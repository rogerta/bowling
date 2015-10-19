(ns bowling.core
  (:gen-class))


; Represents one frame in a bowling game.
;
; :first-pins represents the number of pins knocked down by the first shot.  It
; can have a value from 0 to 10 inclsive.
; :second-pins represents the number of pins knocked down by the second shot.
; It can have a value of 0 to 10 inclusive, or be nil in the case of a partial
; frame or a strike.
; :score represents the total score for game at this frame.  It can have a
; value from 0 to 300.
(defstruct Frame :first-pins :second-pins :score)


(defn is-valid?
  "Checks if a sequence of rolls represents a valid bowling game.

  A sequence of less than 12 must be a game in progress.  This function can
  still check the validity of the sequence.  A sequence or more than 21 is
  definitely invalid.

  Args:
    rolls: a sequence of integers.  Each entry represents the number of pins
        knocked down by that roll.
  Returns:
    Truthy if the sequence of rolls is valid, falsy otherwise.
  "
  [rolls]
  (let [c (count rolls)
        validate #(and (>= % 0) (<= % 10))]
    (cond
      (> c 21) false  ; can't roll more than 21 times
      (= c 0) true  ; the list is empty, this is valid
      (= c 1) (validate (first rolls))  ; one roll
      (= (first rolls) 10) (is-valid? (next rolls))
      (validate (apply + (take 2 rolls))) (is-valid? (nthnext rolls 2))
      :else false)))


(defn game-over?
  "Is the game over?

  A game has 10 frames.  It is over if both shots of the last frame have been
  played.

  If the last frame is either a spare or a strike, then an 11th frame is
  possible.  In this case the game is over if the 10th frame is a spare and
  the first shot of the 11th was played, or if the 10th frame is a strike
  and both shots of the 11th frame were played.

  Args:
    frames: A sequence of Frame structs as returned by |build-frames|.
  Returns:
    Truthy if the game is over, falsy otherwise.
  "
  [frames]
  (let [c (count frames)]
    (cond
      (< c 10) false
      (= c 10)
        (let [last-frame (last frames),
              {f :first-pins s :second-pins} last-frame,
              is-strike (= f 10),
              is-spare (and (not= s nil) (= (+ f s) 10))]
          (if (or is-strike is-spare)
            false
            (not= s 0)))
      :else
        (let [[second-last-frame last-frame] (take-last 2 frames),
              {sl-f :first-pins sl-s :second-pins} second-last-frame,
              is-strike (= sl-f 10),
              is-spare (and (not= sl-s nil) (= (+ sl-f sl-s) 10))
              {f :first-pins s :second-pins} last-frame]
          (cond
            is-strike (not= s nil)
            is-spare true
            :else true)))))  ; in theory, should never get to this line.


(defn frame-score
  "Calculates the score of a frame.

  Assumes the rolls for the frame start at the beginning of the |rolls|
  argument.  This function properly calculates the score for strike and
  spare frames.

  Args:
    rolls: A sequence of integers representing rolls.  It is assumed the
        sequence is valid as given by |is-valid?|.
  Returns:
    The score for the frame.
  "
  [rolls]
  (let [c (count rolls)]
    (cond
      (= c 0) 0
      (= c 1) (first rolls)
      (= (first rolls) 10) (apply + (take 3 rolls))  ; strike
      :else (let [sum (apply + (take 2 rolls))]
            (if (= sum 10)
              (+ sum (nth rolls 2 0))  ; spare
              sum)))))


(defn build-frames
  "Builds a sequence of frames from a sequence of rolls.

  Args:
    rolls: A sequence of integers representing rolls.  It is assumed the
        sequence is valid as given by |is-valid?|.
  Returns:
    A sequence of Frame structures.  The list will be at most 11 frames.
    An 11th frame is retured only if the 10th is a spare or strike.
  "
  [rolls]
  (if (empty? rolls)
    ()
    (let [froll (first rolls), fscore (frame-score rolls)]
      (if (= froll 10)
        (cons (struct Frame froll nil fscore)
              (build-frames (next rolls)))
        (cons (struct Frame froll (nth rolls 1 nil) fscore)
              (build-frames (nthnext rolls 2)))))))


(defn print-frames
  "Prints frames for the game.

  Args:
    rolls: A sequence of integers representing rolls.  It is assumed the
        sequence is valid as given by |is-valid?|.
  "
  [rolls]
  (let [frames (build-frames rolls)]
    (println)
    (dorun (map #(printf "%-4d|" %) (range (count frames))))
    (println)
    (dorun (map #(printf "%d  %d|" (:first-pins %) (:second-pins %)) frames))
    (println)
    (dorun (map #(printf "%3d |" (:score %)) frames))
    (println)
    (println)))


(def ^:dynamic *rolls* [])

(defn -main
  "Bowling score keeper."
  [& args]
  (println "Welcome to the bowling score keeper.")
  (println)
  (while
    (let [_ (print "Enter # of pins knocked down (Ctrl+D to exit): "),
          _ (flush),
          line (read-line),
          roll (if line (Integer/parseInt line) -1),
          new-rolls (conj *rolls* roll)]
      (if (is-valid? new-rolls)
        (do
          (def ^:dynamic *rolls* new-rolls)
          (print-frames *rolls*))
        (if line
          (println roll "is an invalid roll.  Please try again.")
          (println)))
      line)))

