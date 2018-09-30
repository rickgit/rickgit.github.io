# SparseMap����

> SparseArrays map integers to Objects.  Unlike a normal array of Objects, there can be gaps in the indices.  It is intended to be more memory efficient than using a HashMap to map Integers to Objects, both because it avoids auto-boxing keys and its data structure doesn't rely on an extra entry object for each mapping.
> ����[ϡ������](http://hi.baidu.com/piaopiao_0423/item/d8cc2b99729f8380581461d1)

@(Դ�����)[SparseMap|Android]

**SparseMap���������¼������

- **SparseMap��������ʼ��**
- **�洢��ֵ��**
- **�Ƴ���ֵ��**
- **��������**

---------------------

[TOC]

## SparseMap ��������ʼ��
�������
```java
    private static final Object DELETED = new Object();
    private boolean mGarbage = false;//�ж���ɾ��

    private int[] mKeys;  
    private Object[] mValues;
    private int mSize;//���е�����
```

SparseArrays ���������캯����������ֻ�����޲ι��캯����
```java
    public SparseArray() {
        this(10);
    }

    /**
     * Creates a new SparseArray containing no mappings that will not
     * require any additional memory allocation to store the specified
     * number of mappings.  If you supply an initial capacity of 0, the
     * sparse array will be initialized with a light-weight representation
     * not requiring any additional array allocations.
     */
    public SparseArray(int initialCapacity) {
        if (initialCapacity == 0) {
            mKeys = EmptyArray.INT;
            mValues = EmptyArray.OBJECT;
        } else {
            mValues = ArrayUtils.newUnpaddedObjectArray(initialCapacity);
            mKeys = new int[mValues.length];
        }
        mSize = 0;
    }
```
Ĭ�Ͽ��� 10 ��λ���ڴ�ռ� 

##�洢��ֵ��
```java
    public void put(int key, E value) {
        int i = ContainerHelpers.binarySearch(mKeys, mSize, key);

        if (i >= 0) {
            mValues[i] = value;
        } else {
            i = ~i;

            if (i < mSize && mValues[i] == DELETED) {
                mKeys[i] = key;
                mValues[i] = value;
                return;
            }

            if (mGarbage && mSize >= mKeys.length) {
                gc();

                // Search again because indices may have changed.
                i = ~ContainerHelpers.binarySearch(mKeys, mSize, key);
            }

            mKeys = GrowingArrayUtils.insert(mKeys, mSize, i, key);
            mValues = GrowingArrayUtils.insert(mValues, mSize, i, value);
            mSize++;
        }
    }
```
1. ���ֲ��� mkeys���ж��Ƿ��д��� key,�������ã�û������һ��
2. �ж������ҵ��Ƿ����Ѿ����Ϊ*DELETED*,�����򸲸ǣ�������һ��
3. �� mGarbage Ϊ�棬�Ҵ�С�Ѿ�����mKeys�Ĵ�С�������� gc ����, gc ������û�б��Ϊ*DELETED*���ǣ�����ǰ���б��*DELETED*�����²���mkey
4. �����µ�*mKeys*,*mValues*,*mSize*
```java
    private void gc() {
        // Log.e("SparseArray", "gc start with " + mSize);

        int n = mSize;
        int o = 0;
        int[] keys = mKeys;
        Object[] values = mValues;

        for (int i = 0; i < n; i++) {
            Object val = values[i];

            if (val != DELETED) {
                if (i != o) {
                    keys[o] = keys[i];
                    values[o] = val;
                    values[i] = null;
                }

                o++;
            }
        }

        mGarbage = false;
        mSize = o;

        // Log.e("SparseArray", "gc end with " + mSize);
    }
```

## �Ƴ���ֵ��
remove��ʼ����
```java
    public void remove(int key) {
        delete(key);
    }
    public void delete(int key) {
        int i = ContainerHelpers.binarySearch(mKeys, mSize, key);

        if (i >= 0) {
            if (mValues[i] != DELETED) {
                mValues[i] = DELETED;
                mGarbage = true;
            }
        }
    }
```

## ��������
- clear ���mArray����
```java
    public void clear() {
        int n = mSize;
        Object[] values = mValues;

        for (int i = 0; i < n; i++) {
            values[i] = null;
        }

        mSize = 0;
        mGarbage = false;
    }

``` 
