package main

import (
	"bufio"
	"fmt"
	"io"
	"os"
	"regexp"
	"strings"
)

func main() {
	part1()
}

func part1() {
	f, _ := os.Open("inputs/4.txt")
	// f := strings.NewReader(ex1)

	work(f)
}

var ex1 = `ecl:gry pid:860033327 eyr:2020 hcl:#fffffd
byr:1937 iyr:2017 cid:147 hgt:183cm

iyr:2013 ecl:amb cid:350 eyr:2023 pid:028048884
hcl:#cfa07d byr:1929

hcl:#ae17e1 iyr:2013
eyr:2024
ecl:brn pid:760753108 byr:1931
hgt:179cm

hcl:#cfa07d eyr:2025 pid:166559648
iyr:2011 ecl:brn hgt:59in`

func work(f io.Reader) {
	pports := getPassports(f)
	var valid int
	for _, pp := range pports {
		if isValid(pp) {
			valid++
		}
	}
	fmt.Println(valid)
}

var fields = []string{"byr:", "iyr:", "eyr:", "hgt:", "hcl:", "ecl:", "pid:", "cid:"}

func isValid(pp string) bool {
	r := regexp.MustCompile(`(\w{3}):`)
	matches := r.FindAllString(pp, -1)
	mhash := make(map[string]bool)

	for _, m := range matches {
		mhash[m] = true
	}

	for _, field := range fields {
		if field == "cid:" {
			continue
		}
		if mhash[field] != true {
			return false
		}
	}
	return true
}

func getPassports(f io.Reader) []string {
	s := bufio.NewScanner(f)
	var pplines []string
	var pports []string
	for s.Scan() {
		line := s.Text()
		if line == "" {
			pport := strings.Join(pplines, " ")
			pports = append(pports, pport)
			pplines = []string{}
		} else {
			pplines = append(pplines, line)
		}
	}
	pport := strings.Join(pplines, " ")
	pports = append(pports, pport)
	return pports
}
