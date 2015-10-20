# bowling

This project explores idiomatic [Clojure](http://clojure.org/) via a relatively
simple task: scoring the game of bowling.  I've been using this task as a way
to learn new programming languages since 1993, when I used it to learn Java.
[Scoring bowling](https://en.wikipedia.org/wiki/Ten-pin_bowling#Scoring) is
just weird enough that its code is non trivial.

As this is my first clojure program, it's probably not as idiomatic as it could
be.  Feel free to open bugs against style or even send me pull requests with
improvements :-)

## Installation

First install [Clojure](http://clojure.org/getting_started).
Next install [Leiningen](http://leiningen.org/#install).

Finally clone the repository and test it:

    $ git clone git@github.com:rogerta/bowling.git
    $ cd bowling
    $ lein test

    lein test bowling.core-test

    Ran 4 tests containing 42 assertions.
    0 failures, 0 errors.

If you see the message above you're good to go.

## Usage

Score a bowling game from within the development environment with this command:

    $ lein trampoline run

(Note that `lein run` by itself won't work since lein consumes stdin.)

When prompted, enter the number of pins knocked down by one shot.  The program
then displays a textual representation of the frames played.  Repeat until the
game is over.

## License

Copyright © 2015 Roger Tawa
Distributed under Apache License 2.0.

