(ns bowling.core
  (:gen-class))


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
      true false)))


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
      true (let [sum (apply + (take 2 rolls))]
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
    An 11th frame is retured only if the 10th is a spare ot strike.
  "
  [rolls]
  (if (empty? rolls)
    ()
    (let [froll (first rolls), fscore (frame-score rolls)]
      (if (= froll 10)
        (cons (struct Frame froll 0 fscore)
              (build-frames (next rolls)))
        (cons (struct Frame froll (nth rolls 1 0) fscore)
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
    (dorun (map-indexed (fn [i _] (printf "%-4d|" (int i))) frames))
    (println)
    (dorun (map (fn [f] (printf "%d  %d|" (:first-pins f) (:second-pins f)))
                frames))
    (println)
    (dorun (map (fn [f] (printf "%3d |" (:score f))) frames))
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

