#UrlEncode

>
 
@(源码分析)[OkHttp3.2|Android]

 

- **创建及初始化**
- **connection** 

---------------------

[TOC]

## 
### 
openjdk 实现的代码比较易懂

```java
    public static String encode(String s, String enc)
        throws UnsupportedEncodingException {

        boolean needToChange = false;
        StringBuffer out = new StringBuffer(s.length());
        Charset charset;
        CharArrayWriter charArrayWriter = new CharArrayWriter();

        if (enc == null)
            throw new NullPointerException("charsetName");

        try {
            charset = Charset.forName(enc);
        } catch (IllegalCharsetNameException e) {
            throw new UnsupportedEncodingException(enc);
        } catch (UnsupportedCharsetException e) {
            throw new UnsupportedEncodingException(enc);
        }

        for (int i = 0; i < s.length();) {
            int c = (int) s.charAt(i);
            //System.out.println("Examining character: " + c);
            if (dontNeedEncoding.get(c)) {
                if (c == ' ') {
                    c = '+';
                    needToChange = true;
                }
                //System.out.println("Storing: " + c);
                out.append((char)c);
                i++;
            } else {
                // convert to external encoding before hex conversion
                do {
                    charArrayWriter.write(c);
                    /*
                     * If this character represents the start of a Unicode
                     * surrogate pair, then pass in two characters. It's not
                     * clear what should be done if a bytes reserved in the
                     * surrogate pairs range occurs outside of a legal
                     * surrogate pair. For now, just treat it as if it were
                     * any other character.
                     */
                    if (c >= 0xD800 && c <= 0xDBFF) {
                        /*
                          System.out.println(Integer.toHexString(c)
                          + " is high surrogate");
                        */
                        if ( (i+1) < s.length()) {
                            int d = (int) s.charAt(i+1);
                            /*
                              System.out.println("\tExamining "
                              + Integer.toHexString(d));
                            */
                            if (d >= 0xDC00 && d <= 0xDFFF) {
                                /*
                                  System.out.println("\t"
                                  + Integer.toHexString(d)
                                  + " is low surrogate");
                                */
                                charArrayWriter.write(d);
                                i++;
                            }
                        }
                    }
                    i++;
                } while (i < s.length() && !dontNeedEncoding.get((c = (int) s.charAt(i))));

                charArrayWriter.flush();
                String str = new String(charArrayWriter.toCharArray());
                byte[] ba = str.getBytes(charset);
                for (int j = 0; j < ba.length; j++) {
                    out.append('%');
                    char ch = Character.forDigit((ba[j] >> 4) & 0xF, 16);
                    // converting to use uppercase letter as part of
                    // the hex value if ch is a letter.
                    if (Character.isLetter(ch)) {
                        ch -= caseDiff;
                    }
                    out.append(ch);
                    ch = Character.forDigit(ba[j] & 0xF, 16);
                    if (Character.isLetter(ch)) {
                        ch -= caseDiff;
                    }
                    out.append(ch);
                }
                charArrayWriter.reset();
                needToChange = true;
            }
        }

        return (needToChange? out.toString() : s);
    }
```

android 源码位置：libcore.net.UriCodec

```android
    private void appendEncoded(StringBuilder builder, String s, Charset charset,
            boolean isPartiallyEncoded) {
        if (s == null) {
            throw new NullPointerException("s == null");
        }

        int escapeStart = -1;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if ((c >= 'a' && c <= 'z')
                    || (c >= 'A' && c <= 'Z')
                    || (c >= '0' && c <= '9')
                    || isRetained(c)
                    || (c == '%' && isPartiallyEncoded)) {
                if (escapeStart != -1) {
                    appendHex(builder, s.substring(escapeStart, i), charset);
                    escapeStart = -1;
                }
                if (c == '%' && isPartiallyEncoded) {
                    // this is an encoded 3-character sequence like "%20"
                    builder.append(s, i, Math.min(i + 3, s.length()));
                    i += 2;
                } else if (c == ' ') {
                    builder.append('+');
                } else {
                    builder.append(c);
                }
            } else if (escapeStart == -1) {
                escapeStart = i;
            }
        }
        if (escapeStart != -1) {
            appendHex(builder, s.substring(escapeStart, s.length()), charset);
        }
    }
    private static void appendHex(StringBuilder builder, String s, Charset charset) {
        for (byte b : s.getBytes(charset)) {
            appendHex(builder, b);
        }
    }

    private static void appendHex(StringBuilder sb, byte b) {
        sb.append('%');
        sb.append(Byte.toHexString(b, true));
    }
```
java.lang.IntegralToString#byteToHexString
```java
    public static String byteToHexString(byte b, boolean upperCase) {
        char[] digits = upperCase ? UPPER_CASE_DIGITS : DIGITS;
        char[] buf = new char[2]; // We always want two digits.
        buf[0] = digits[(b >> 4) & 0xf];
        buf[1] = digits[b & 0xf];
        return new String(0, 2, buf);
    }

```