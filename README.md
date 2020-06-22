﻿# Sokoban 1.7.5

# Demo:
https://youtu.be/ItoZPud7C1g

# BUGS:
警衛重合問題 \
如果把bag推進goal內雖然achivement會增加，但是又把bag推出去的話achivement不會減少 \


# 提案或建議:
要不要警衛被逼進死較就把他free掉(指向null)?省得麻煩\
角色選擇決定功能物品配置\
(有空再做)地獄模式特殊結局:達成所有貨物後，不結束遊戲，而是生出6隻警衛，並在地圖另一頭開一個口，目標變成逃出房間\
每關勝利的話有個win的特效 \
saves.txt要不要讓玩家看不懂裡面的內容?不然玩家可以直接修改數據 \


# Bugs Archive:
讀不到圖問題:修改資料夾層級\
傳送門之人物蓋住貨物神奇bug:改成上面有貨不給傳\
子彈可以穿過地圖:改成不能穿\
穿牆穿出地圖\
人物正面碰到警衛後僅把visible設false，遊戲未關閉\
程式碼出現不雅字眼\
關卡complete會當掉


# Version History
v1.0.0 - 基礎遊戲完成\
v1.0.1 - 修正貨物可推至hardWall及穿牆可多次使用的bug\
v1.1.0 - 警衛版一更改 增加警衛class 在board裡增加checkBagCollisionforPolice method\
v1.1.1 - 圖片更改；圖片封裝優先度更改，警衛的圖片會顯示在較上層\
v1.2.0 - 新增地圖，更改路徑。\
v1.2.1 - 將警衛改為不可穿牆。為debug將穿牆功能暫時改為按一次即可永久穿牆，再按一次恢復，交替運作\
v1.2.2 - 配合eclipse修改圖片載入路徑\
v1.3.0 - 人物會依據移動方向改變圖片方向；將穿牆功能改為X鍵、傳送點功能改為Z鍵，更改package\
v1.3.1 - 警衛碰到子彈就消失，人物正面碰到警衛遊戲結束\
v1.3.2 - 修改警衛消失後的bug,修改成警衛子彈不同調\
v1.4.0 - 子彈設定成無法連射，難度決定警衛跑速，增加主選單\
v1.4.1 - 新增圖片；精簡部分程式碼；新增一張地圖(地獄)；增加解說；警衛改成每走兩步換行進方向，減少混亂感\
v1.4.2 - 現在關卡完成後遊戲會正確關閉了\
v1.4.3 - 對齊貨物至格子中間，削減部分程式碼，新增一首BGM(尚未能撥放)，暫時新增二號玩家\
v1.4.4 - 警衛2.0 \
v1.5.0 - 微調 \
v1.5.1 - 讓警衛被困住時消失，子彈一開始就有，怕警衛太多沒辦法攻擊，每疊完一個bag加3個子彈 \
v1.6.0 - 最終調整 \
v1.6.1 - bugs fixed \
v1.6.2 - 增加音樂 \
v1.6.3 - 新增兩首音樂，每關有不同音樂 \
v1.6.4 - 子彈每多放一個baggage增加兩發，更改地圖 \
v1.6.5 - 調整地圖以及轉向，連續按「左上右右下左右上」的話....? \
v1.7.0 - 警衛3.0，每個警衛有自己不同的行為模式。程式碼、解說及地圖的修正。把人物圖片改好看了 \
v1.7.1 - 更改主選單外觀，現在選擇關卡會移到下一頁。新增存檔系統，關卡解鎖進度可以存在硬碟(saves.txt) \
v1.7.2 - Board更名為Stage。現在輸了會直接重新開始；而贏了進入下一關。按ESC可以暫停，再按一次繼續 \
v1.7.3 - 新增警衛四方向圖片；新增標示主角的箭頭；新增一個主角，更改子彈、寶箱、牆壁、傳送門(動畫)的圖片 \
v1.7.4 - 解決子彈抖動以及穿過警衛的問題；改成全螢幕；pause功能變成選單；新增遊戲破關文字(尚未顯示)\
v1.7.5 - 現在玩數字小於存點的關卡不會覆蓋儲存了；抓到一個小bug;新增3收音樂;現在主選單第一次載入時有音樂


# 程式內變數解釋
forbottom:為了防止當sokoban 裡的timer refresh repaint 時和點按鍵時的repaint 重複
