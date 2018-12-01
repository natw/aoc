package main

import (
	"fmt"
	"io/ioutil"
	"strconv"
	"strings"
)

func main() {
	lines := readFile("inputs/input1.txt")
	// lines := []string{"+7", "+7", "-2", "-7", "-4"}
	findSeenAgain(lines)
}

func findSeenAgain(lines []string) {
	seenFreqs := make(map[int]bool)
	freq := 0
	seenFreqs[freq] = true

	for {
		for _, line := range lines {
			if line == "" {
				continue
			}
			delta, err := strconv.Atoi(line)
			check(err)

			newFreq := freq + delta
			_, seen := seenFreqs[newFreq]
			// fmt.Printf("freq: %d + %d = %d (%s)\n", freq, delta, newFreq, seen)

			if seen == true {
				fmt.Println("Saw again!!!", newFreq)
				return
			}
			seenFreqs[newFreq] = true
			freq = newFreq

		}
	}
}

func check(err error) {
	if err != nil {
		panic(err)
	}
}

func readFile(filename string) []string {
	contents, err := ioutil.ReadFile(filename)
	check(err)

	lines := strings.Split(string(contents), "\n")
	return lines
}
