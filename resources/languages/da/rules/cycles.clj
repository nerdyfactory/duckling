; Cycles are like a heart beat, the next starts just when the previous ends.
; Unlike durations, they have an absolute position in the time, it's just that this position is periodic.
; Examples of phrases involving cycles:
; - this week
; - today (= this day)
; - last month
; - last 2 calendar months (last 2 months is interpreted as a duration)
;
; As soon as you put a quantity (2 months), the cycle becomes a duration.


(
  "second (cycle)"
  #"(?i)sekund(er)?"
  {:dim :cycle
   :grain :second}

  "minute (cycle)"
  #"(?i)minut(ter)?"
  {:dim :cycle
   :grain :minute}

  "hour (cycle)"
  #"(?i)time(r)?"
  {:dim :cycle
   :grain :hour}

  "day (cycle)"
  #"(?i)dag(e)?"
  {:dim :cycle
   :grain :day}

  "week (cycle)"
  #"(?i)uger?"
  {:dim :cycle
   :grain :week}

  "month (cycle)"
  #"(?i)måned(er)?"
  {:dim :cycle
   :grain :month}
  
  "quarter (cycle)"
  #"(?i)kvartal(er)?"
  {:dim :cycle
   :grain :quarter}
  
  "year (cycle)"
  #"(?i)år"
  {:dim :cycle
   :grain :year}
  
  "this <cycle>"
  [#"(?i)denne|dette|i|nuværende|indeværende" (dim :cycle)]
  (cycle-nth (:grain %2) 0)

  "last <cycle>"
  [#"(?i)sidste|seneste" (dim :cycle)]
  (cycle-nth (:grain %2) -1)

  "next <cycle>"
  [#"(?i)næste|kommende" (dim :cycle)]
  (cycle-nth (:grain %2) 1)
  
  "the <cycle> after <time>"
  [(dim :cycle) #"(en|tet|et)? (?i)efter" (dim :time)]
  (cycle-nth-after (:grain %1) 1 %3)

  "<cycle> after <time>"
  [(dim :cycle) #"(?i)efter" (dim :time)]
  (cycle-nth-after (:grain %1) 1 %3)
  
  "the <cycle> before <time>"
  [(dim :cycle) #"(en|tet|et)? (?i)før" (dim :time)]
  (cycle-nth-after (:grain %1) -1 %3)
  
  "<cycle> before <time>"
  [(dim :cycle) #"(?i)før" (dim :time)]
  (cycle-nth-after (:grain %1) -1 %3)

  "last n <cycle>"
  [#"(?i)sidste|seneste" (integer 1 9999) (dim :cycle)]
  (cycle-n-not-immediate (:grain %3) (- (:value %2)))
  
  "next n <cycle>"
  [#"(?i)næste" (integer 1 9999) (dim :cycle)]
  (cycle-n-not-immediate (:grain %3) (:value %2))
  
  "<ordinal> <cycle> of <time>"
  [(dim :ordinal) (dim :cycle) #"(?i)af|i|fra" (dim :time)]
  (cycle-nth-after-not-immediate (:grain %2) (dec (:value %1)) %4)
  
  "the <ordinal> <cycle> of <time>"
  [#"(?i)den" (dim :ordinal) (dim :cycle) #"(?i)af|i|fra" (dim :time)]
  (cycle-nth-after-not-immediate (:grain %3) (dec (:value %2)) %5)

  "the <cycle> of <time>"
  [(dim :cycle) #"(en|tet|et)? (?i)af" (dim :time)]
  (cycle-nth-after-not-immediate (:grain %1) 0 %3)

  ; the 2 following rules may need a different helper
  
  "<ordinal> <cycle> after <time>"
  [(dim :ordinal) (dim :cycle) #"(?i)efter" (dim :time)]
  (cycle-nth-after-not-immediate (:grain %2) (dec (:value %1)) %4)
  
  "the <ordinal> <cycle> after <time>"
  [#"(?i)den" (dim :ordinal) (dim :cycle) #"(?i)efter" (dim :time)]
  (cycle-nth-after-not-immediate (:grain %3) (dec (:value %2)) %5)

  
  ; quarters are a little bit different, you can say "3rd quarter" alone
  
  "<ordinal> quarter"
  [(dim :ordinal) (dim :cycle #(= :quarter (:grain %)))]
  (cycle-nth-after :quarter (dec (:value %1)) (cycle-nth :year 0))

  "<ordinal> quarter <year>"
  [(dim :ordinal) (dim :cycle #(= :quarter (:grain %))) (dim :time)]
  (cycle-nth-after :quarter (dec (:value %1)) %3)
)
