Akka Meter Simulator
=========================
A very minimal meter simulator.

To run from your Mac:
* Prep
  * Install Homebrew - see http://brew.sh/
  * Install Scala
    ```sh
    brew install scala
    ```
  * Install sbt (Scala Build Tool)
    ```sh
    brew install sbt
    ```
  * Clone this repo (!)

* Build and Run
  ```sh
  # from the base directory of the project
  sbt assembly
  # if -DnumMeters is not supplied, 10 meters will be created by default
  java -DnumMeters=2 -jar target/scala-2.11/akka-meter-simulator-assembly-1.0.jar
  ```





