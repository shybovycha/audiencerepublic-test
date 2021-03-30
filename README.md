# Test task

## Usage

```bash
$ lein repl
```

and then, in REPL

```clojure
;; note the below function might generate non-acyclic graph, so longest path might not work (as well as eccentrity for such graph)
(def G (make-graph 10 10))
(shortest-path G :1 :10)
(longest-path G :1 :10)
(radius G)
(diameter G)
```

## License

Copyright © 2021 Artem Shubovych

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
