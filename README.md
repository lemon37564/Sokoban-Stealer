﻿# Sokoban-Stealer 1.11.1
倉庫番大盜\
1082 java程式語言

# Demo(1.6.0):
https://youtu.be/ItoZPud7C1g \
demo後新增的部分請看下方的版本歷史\
操作請看遊戲內說明

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
v1.7.5 - 現在玩數字小於存點的關卡不會覆蓋儲存了；抓到一個小bug;新增3收音樂;現在主選單第一次載入時有音樂\
v1.7.6 - 死亡延遲及死亡動畫，死亡時不瞬間跳輸的畫面，比較不會不明所以\
v1.8.0 - 對所有解析度進行適配，現在螢幕解析度再低都不會超出畫面了；順便加了一些東西 \
v1.8.1 - 新增兩張地圖，暫停畫面修飾；actionListener微調\
v1.8.2 - 作弊2，連續按下"sokoban"的話...?\
v1.8.3 - 圖片統一管理，不用再重複包裝圖片，增快速度(原本會有些許延遲)\
v1.9.0 - 增加地圖；修復達成箱子的bug；遊戲中文化；增加遊戲全部過關的動畫\
v1.9.1 - 修正同時撥多首歌問題(可能沒修有好)\
v1.9.2 - 修正同時撥多首歌問題(修好了啦幹)。起因為go next stage未預期的重複執行，現在gonextstage只會做一次\
v1.9.3 - v1.9.1的問題從根基澈底解決；新增結尾動畫音樂；全破之後可以在主畫面看到結尾動畫\
v1.9.4 - 音樂好像還是偶爾會有問題；調整結束動畫 \
v1.9.5 - 找到其中一個音樂bug並修復；結束動畫調整\
v1.9.6 - 地圖做完了(9張，順序有調過)；結束動畫調整；stage微調\
v1.9.7 - 結尾動畫調整\
v1.9.8 - 做出executable jar\
v1.9.9 - #\
v1.10.0 - refactor\
v1.10.1 - 調整一些東西和邏輯\
v1.11.0 - 刪除及修改不好的程式碼\
v1.11.1 - 修改一些東西
