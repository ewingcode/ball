SELECT account,gold AS '下注金额',COUNT(1) AS '场数' ,SUM(win_gold) AS '结果' 
FROM `bet_bill` WHERE SUBSTR(w_id,3) IN (SELECT ticket_id FROM bet_log WHERE bet_rule_id IS NOT NULL) AND  CONCAT(`date`,' ',`addtime`) >= '2018-11-19' GROUP BY account

 

SELECT * FROM (
SELECT '赢',account,gold AS '下注金额',COUNT(1) AS '场数' ,SUM(win_gold) AS '结果' 
FROM `bet_bill` WHERE SUBSTR(w_id,3) IN (SELECT ticket_id FROM bet_log WHERE bet_rule_id IS NOT NULL) AND CONCAT(`date`,' ',`addtime`) >= '2018-11-19' AND win_gold>0 GROUP BY account
UNION ALL 
SELECT '输',account,gold AS '下注金额',COUNT(1) AS '场数' ,SUM(win_gold) AS '结果' 
FROM `bet_bill` WHERE SUBSTR(w_id,3) IN (SELECT ticket_id FROM bet_log WHERE bet_rule_id IS NOT NULL) AND CONCAT(`date`,' ',`addtime`) >= '2018-11-19' AND win_gold<0 GROUP BY account
) a ORDER BY a.account 


SELECT create_time,gid, (SELECT sc_total FROM bet_info i WHERE i.gid=l.gid) total, SUBSTR(buy_desc,130),buy_desc  FROM bet_log l WHERE account='tansonLAM83' AND CODE!=560 GROUP BY gid ORDER BY id DESC 


UPDATE `bet_rule` SET param=REPLACE(param,'"BUY_WAY":"1"','"BUY_WAY":"0"');


SELECT l.create_time,l.league,l.gid, i.sc_total,i.sc_FT_A,i.sc_FT_H, SUBSTR(buy_desc,100),buy_desc FROM bet_log l,bet_info i
 
WHERE i.gid=l.gid AND account='tansonLAM83' AND CODE=560 GROUP BY l.gid ORDER BY l.id DESC 