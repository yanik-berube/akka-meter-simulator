Akka Meter Simulator
=========================
A very minimal meter simulator.

To run from your Mac:
* Prep
  * Install Homebrew - see http://brew.sh/
  * Install Scala
    ```
    brew install scala
    ```
  * Install sbt (Scala Build Tool)
    ```
    brew install sbt
    ```
  * Clone this repo (!)

* Build and Run
  ```sh
  # from the base directory of the project
  sbt assembly
  java -DnumMeters=10000 -DnumDigits=6 -DblurtFreq=15 -jar target/scala-2.11/akka-meter-simulator-assembly-1.0.jar
  
  # -DnumMeters: number of meters to simulate. Defaults to 1000 if not supplied.
  # -DnumDigits: number of digits for the meter's faceplate. Defaults to 6 if not supplied.
  # -DblurtFreq: blurt frequency in minutes: Defaults to 5 minutes if not supplied.
  ```





