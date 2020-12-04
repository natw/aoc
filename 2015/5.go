package main

import (
	"bufio"
	"fmt"
	"os"
	"strings"
)

func main() {
	part1()
}

func part1() {
	f, _ := os.Open("inputs/5.txt")
	s := bufio.NewScanner(f)
	nice := 0
	for s.Scan() {
		line := s.Text()
		if IsNice1(line) {
			nice++
		}
	}
	fmt.Println(nice)

	// check1("ugknbfddgicrmopn")
	// check1("aaa")
	// check1("jchzalrnumimnmhp")
	// check1("haegwjzuvuyypxyu")
	// check1("dvszwmarrgswjxmb")
}

func check1(ex1 string) {
	fmt.Printf("%s - %t\n", ex1, IsNice1(ex1))
}

func IsNice1(word string) bool {
	if hasThreeVowels(word) && twiceInARow(word) && noForbidden(word) {
		return true
	}
	return false
}

func hasThreeVowels(word string) bool {
	hasVowels := 0
	vowels := []string{"a", "e", "i", "o", "u"}
	for _, v := range vowels {
		hasVowels += strings.Count(word, v)
	}

	if hasVowels >= 3 {
		return true
	} else {
		return false
	}
}

func twiceInARow(word string) bool {
	for i, letter := range word[:len(word)-1] {
		if word[i+1] == byte(letter) {
			return true
		}
	}
	return false
}

func noForbidden(word string) bool {
	verboten := []string{"ab", "cd", "pq", "xy"}
	for _, v := range verboten {
		if strings.Contains(word, v) {
			return false
		}
	}
	return true
}
