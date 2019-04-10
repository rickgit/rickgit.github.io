##层叠样式表（英语：Cascading Style Sheets，简写CSS）

- 重用样式，易于维护，兼容性

```
+---------------------------+-----------------------------------+---------------------------+
|                           |   Layers                          |                           |
|pseudo-element pseudo-class|   Positioning                     |                           |
|                           |   Visibility                      |                           |
+-------------------------+-+---------------+-------------------+                           |
| Type       Universal *  |                 |                   |                           |
|                         |Scrollbars       |                   |                           |
| Descendant Class .      |Dimension        |                   |                           |
|                         |Outlines         |                   |                           |
| ID #   Child >          |Paddings Cursors |                   |                           |
|                         |Margins Lists    |                   |                           |
| Attribute [type = ""]   |Tables Borders   |                   |                           |
|                         |Images Links     |                   |                           |
| Grouping ,              |Fonts  Text      |  Unit  Colors     |                           |
|                         |Colors background|                   |                           |
+---------------------------------------------------------------+ @font-face !important     |
|                selector { property: value }                   | @import    @charset       |
+-------------------------------------------------------------------------------------------+
|            Comments /*  */                                    |       @ Rules             |
+---------------------------------------------------------------+---------------------------+
|                                                                                           |
|  style=""         <style>         <link>         @import                                  |
| Inline CSS      Embedded CSS    External CSS   Imported CSS                               |
|                                                                                           |
+-------------------------------------------------------------------------------------------+
```