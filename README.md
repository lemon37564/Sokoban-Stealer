﻿# Sokoban 1.0.1

# Bugs:
  none
 有些地圖會爆走

# 提案或建議:
感覺穿牆發動後可以加一個顯示剩餘時間的提示，比較不會緊張(?\
人物可以轉動方向

# 尚未開發完成項目:
警衛\
主選單\
難度選擇\
關卡開發\
音效選擇

# Bugs Archive:
讀不到圖問題:修改資料夾層級\
傳送門之人物蓋住貨物神奇bug:改成上面有貨不給傳\
子彈可以穿過地圖:改成不能穿\
穿牆穿出地圖

# Version History
v1.0.0 - NULL\
v1.0.1 - 修正貨物可推至hardWall及穿牆可多次使用的bug
v1.0.2 - 警衛版一更改 增加警衛class 在board裡增加checkBagCollisionforPolice method 
v1.0.3 - 改變推箱子bug 增加map
#程式內變數解釋
 forbottom:為了防止當sokoban 裡的timer refresh repaint 時和點按鍵時的repaint 重複
