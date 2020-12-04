package main

import (
	"bufio"
	"fmt"
	"io"
	"os"
	"regexp"
	"strconv"
	"strings"
)

func main() {
	part2()
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

//--------

var ex21 = `eyr:1972 cid:100
hcl:#18171d ecl:amb hgt:170 pid:186cm iyr:2018 byr:1926

iyr:2019
hcl:#602927 eyr:1967 hgt:170cm
ecl:grn pid:012533040 byr:1946

hcl:dab227 iyr:2012
ecl:brn hgt:182cm pid:021572410 eyr:2020 byr:1992 cid:277

hgt:59cm ecl:zzz
eyr:2038 hcl:74454a iyr:2023
pid:3556412378 byr:2007`

var ex22 = `pid:087499704 hgt:74in ecl:grn iyr:2012 eyr:2030 byr:1980
hcl:#623a2f

eyr:2029 ecl:blu cid:129 byr:1989
iyr:2014 pid:896056539 hcl:#a97842 hgt:165cm

hcl:#888785
hgt:164cm byr:2001 iyr:2015 cid:88
pid:545766238 ecl:hzl
eyr:2022

iyr:2010 hgt:158cm hcl:#b6652a ecl:blu byr:1944 eyr:2021 pid:093154719`

func part2() {
	f, _ := os.Open("inputs/4.txt")
	// f := strings.NewReader(ex22)

	r := regexp.MustCompile(`(?P<key>\w{3}): ?(?P<value>\S+)`)
	pportLines := getPassports(f)
	valid := 0
	for _, line := range pportLines {
		pport := MapLine(line, r)
		if isValid2(pport) {
			valid++
		}
	}
	fmt.Println(valid)
}

func isValid2(pport map[string]string) bool {
	// fmt.Printf("checking: %+v\n", pport)
	byr, err := strconv.ParseInt(pport["byr"], 10, 64)
	if err != nil {
		// fmt.Printf("%s gave error: %v\n", pport["byr"], err)
		return false
	}
	if byr < 1920 || byr > 2002 {
		return false
	}

	iyr, err := strconv.ParseInt(pport["iyr"], 10, 64)
	if err != nil {
		// fmt.Printf("%s gave error: %v\n", pport["iyr"], err)
		return false
	}
	if iyr < 2010 || iyr > 2020 {
		return false
	}

	eyr, err := strconv.ParseInt(pport["eyr"], 10, 64)
	if err != nil {
		return false
	}

	if eyr < 2020 || eyr > 2030 {
		return false
	}

	height := pport["hgt"]
	if strings.Contains(height, "cm") {
		h, err := strconv.ParseInt(height[:len(height)-2], 10, 64)
		// fmt.Printf("height: %s parsed: %d\n", height, h)
		if err != nil {
			return false
		}
		if h < 150 || h > 193 {
			return false
		}
	} else if strings.Contains(height, "in") {
		h, err := strconv.ParseInt(height[:len(height)-2], 10, 64)
		// fmt.Printf("height: %s parsed inches: %d\n", height, h)
		if err != nil {
			return false
		}
		if h < 59 || h > 76 {
			return false
		}
	} else {
		return false
	}

	hclR := regexp.MustCompile(`#([0-9]|[a-f]){6}`)
	if !hclR.Match([]byte(pport["hcl"])) {
		// fmt.Println(pport)
		// fmt.Printf("%s is not a color\n", pport["hcl"])
		return false
	}
	// fmt.Printf("%s is a color\n", pport["hcl"])

	ec := pport["ecl"]
	if !(ec == "amb" || ec == "blu" || ec == "brn" || ec == "gry" || ec == "grn" || ec == "hzl" || ec == "oth") {
		return false
	}

	pidR := regexp.MustCompile(`^\d{9}$`)
	if !pidR.Match([]byte(pport["pid"])) {
		// fmt.Printf("%s is not 9 digits\n", pport["pid"])
		return false
	}
	fmt.Printf("%s is 9 digits\n", pport["pid"])
	// fmt.Printf("checked: %+v\n", pport)

	return true
}

func MapLine(line string, r *regexp.Regexp) map[string]string {
	matches := r.FindAllStringSubmatch(line, -1)
	p := make(map[string]string)
	for _, m := range matches {
		p[m[1]] = m[2]
	}
	return p
}
