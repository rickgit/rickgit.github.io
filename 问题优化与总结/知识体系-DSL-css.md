##层叠样式表（英语：Cascading Style Sheets，简写CSS）

- 重用样式，易于维护，兼容性

```
+---------------------------+-----------------------------------+---------------------------+
|                           |   Layers                          |                           |
|pseudo-element             |   Positioning                     |                           |
|pseudo-class               |   Visibility                      |                           |
+-------------------------+-+---------------+-------------------+                           |
| Adjacent sibling +      |                 |                   |                           |
|  Universal *            |Scrollbars       |                   |                           |
| Descendant(space)       |Dimension        |                   |                           |
|  Child >                |Outlines         |                   |                           |
|  Class .                |Paddings Cursors |                   |                           |
| ID #                    |Margins Lists    |                   |                           |
| Attribute [type = ""]   |Tables Borders   |                   |                           |
| Type                    |Images Links     |                   |                           |
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