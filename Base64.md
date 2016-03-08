#Android Base64编码

> Utilities for encoding and decoding the Base64 representation of binary data.  See RFCs <a href="http://www.ietf.org/rfc/rfc2045.txt">2045</a> and <a href="http://www.ietf.org/rfc/rfc3548.txt">3548</a>. ---Android
> Base64 is a group of similar binary-to-text encoding schemes that represent binary data in an ASCII string format by translating it into a radix-64 representation. ---[wiki](https://en.wikipedia.org/wiki/Base64)

@(源码分析)[Base64|Android]

Base64有很多实现，除了Android，apache还有FastJson也有Base64的实现方式，公司里面也有两种编解码  。。！
-- 实现原理如下图，摘抄于wikipedia
-----

<table class="wikitable">
    <tbody>
        <tr>
            <th scope="row">Text content</th>
            <td colspan="8" style="text-align:center;"><b>M</b></td>
            <td colspan="8" style="text-align:center;"><b>a</b></td>
            <td colspan="8" style="text-align:center;"><b>n</b></td>
        </tr>
        <tr>
            <th scope="row">ASCII</th>
            <td colspan="8" style="text-align:center;">77 (0x4d)</td>
            <td colspan="8" style="text-align:center;">97 (0x61)</td>
            <td colspan="8" style="text-align:center;">110 (0x6e)</td>
        </tr>
        <tr>
            <th scope="row">Bit pattern</th>
            <td>0</td>
            <td>1</td>
            <td>0</td>
            <td>0</td>
            <td>1</td>
            <td>1</td>
            <td>0</td>
            <td>1</td>
            <td>0</td>
            <td>1</td>
            <td>1</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
            <td>0</td>
            <td>1</td>
            <td>0</td>
            <td>1</td>
            <td>1</td>
            <td>0</td>
            <td>1</td>
            <td>1</td>
            <td>1</td>
            <td>0</td>
        </tr>
        <tr>
            <th scope="row">Index</th>
            <td colspan="6" style="text-align:center;">19</td>
            <td colspan="6" style="text-align:center;">22</td>
            <td colspan="6" style="text-align:center;">5</td>
            <td colspan="6" style="text-align:center;">46</td>
        </tr>
        <tr>
            <th scope="row">Base64-encoded</th>
            <td colspan="6" style="text-align:center;"><b>T</b></td>
            <td colspan="6" style="text-align:center;"><b>W</b></td>
            <td colspan="6" style="text-align:center;"><b>F</b></td>
            <td colspan="6" style="text-align:center;"><b>u</b></td>
        </tr>
    </tbody>
</table>


-----------

<table class="wikitable" style="text-align:center">
    <tbody>
        <tr>
            <th scope="col">Value</th>
            <th scope="col">Char</th>
            <td rowspan="17">&nbsp;</td>
            <th scope="col">Value</th>
            <th scope="col">Char</th>
            <td rowspan="17">&nbsp;</td>
            <th scope="col">Value</th>
            <th scope="col">Char</th>
            <td rowspan="17">&nbsp;</td>
            <th scope="col">Value</th>
            <th scope="col">Char</th>
        </tr>
        <tr>
            <td>0</td>
            <td><code>A</code></td>
            <td>16</td>
            <td><code>Q</code></td>
            <td>32</td>
            <td><code>g</code></td>
            <td>48</td>
            <td><code>w</code></td>
        </tr>
        <tr>
            <td>1</td>
            <td><code>B</code></td>
            <td>17</td>
            <td><code>R</code></td>
            <td>33</td>
            <td><code>h</code></td>
            <td>49</td>
            <td><code>x</code></td>
        </tr>
        <tr>
            <td>2</td>
            <td><code>C</code></td>
            <td>18</td>
            <td><code>S</code></td>
            <td>34</td>
            <td><code>i</code></td>
            <td>50</td>
            <td><code>y</code></td>
        </tr>
        <tr>
            <td>3</td>
            <td><code>D</code></td>
            <td>19</td>
            <td><code>T</code></td>
            <td>35</td>
            <td><code>j</code></td>
            <td>51</td>
            <td><code>z</code></td>
        </tr>
        <tr>
            <td>4</td>
            <td><code>E</code></td>
            <td>20</td>
            <td><code>U</code></td>
            <td>36</td>
            <td><code>k</code></td>
            <td>52</td>
            <td><code>0</code></td>
        </tr>
        <tr>
            <td>5</td>
            <td><code>F</code></td>
            <td>21</td>
            <td><code>V</code></td>
            <td>37</td>
            <td><code>l</code></td>
            <td>53</td>
            <td><code>1</code></td>
        </tr>
        <tr>
            <td>6</td>
            <td><code>G</code></td>
            <td>22</td>
            <td><code>W</code></td>
            <td>38</td>
            <td><code>m</code></td>
            <td>54</td>
            <td><code>2</code></td>
        </tr>
        <tr>
            <td>7</td>
            <td><code>H</code></td>
            <td>23</td>
            <td><code>X</code></td>
            <td>39</td>
            <td><code>n</code></td>
            <td>55</td>
            <td><code>3</code></td>
        </tr>
        <tr>
            <td>8</td>
            <td><code>I</code></td>
            <td>24</td>
            <td><code>Y</code></td>
            <td>40</td>
            <td><code>o</code></td>
            <td>56</td>
            <td><code>4</code></td>
        </tr>
        <tr>
            <td>9</td>
            <td><code>J</code></td>
            <td>25</td>
            <td><code>Z</code></td>
            <td>41</td>
            <td><code>p</code></td>
            <td>57</td>
            <td><code>5</code></td>
        </tr>
        <tr>
            <td>10</td>
            <td><code>K</code></td>
            <td>26</td>
            <td><code>a</code></td>
            <td>42</td>
            <td><code>q</code></td>
            <td>58</td>
            <td><code>6</code></td>
        </tr>
        <tr>
            <td>11</td>
            <td><code>L</code></td>
            <td>27</td>
            <td><code>b</code></td>
            <td>43</td>
            <td><code>r</code></td>
            <td>59</td>
            <td><code>7</code></td>
        </tr>
        <tr>
            <td>12</td>
            <td><code>M</code></td>
            <td>28</td>
            <td><code>c</code></td>
            <td>44</td>
            <td><code>s</code></td>
            <td>60</td>
            <td><code>8</code></td>
        </tr>
        <tr>
            <td>13</td>
            <td><code>N</code></td>
            <td>29</td>
            <td><code>d</code></td>
            <td>45</td>
            <td><code>t</code></td>
            <td>61</td>
            <td><code>9</code></td>
        </tr>
        <tr>
            <td>14</td>
            <td><code>O</code></td>
            <td>30</td>
            <td><code>e</code></td>
            <td>46</td>
            <td><code>u</code></td>
            <td>62</td>
            <td><code>+</code></td>
        </tr>
        <tr>
            <td>15</td>
            <td><code>P</code></td>
            <td>31</td>
            <td><code>f</code></td>
            <td>47</td>
            <td><code>v</code></td>
            <td>63</td>
            <td><code>/</code></td>
        </tr>
    </tbody>
</table>


----------


- **创建及初始化**
- **存储键值对**
- **获取键值对**
- **移除键值对**
- **清理容器**

---------------------

[TOC]

##创建及初始化


##存储键值对

##移除键值对

##获取键值对

##清理容器