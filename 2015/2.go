package main

import (
	"bufio"
	"fmt"
	"os"
	"regexp"
	"strconv"
)

func MapLines(fname string, regex string) []map[string]string {
	r := regexp.MustCompile(regex)

	f, _ := os.Open(fname)
	defer f.Close()
	s := bufio.NewScanner(f)
	var lines []map[string]string
	for s.Scan() {
		line := s.Text()
		match := r.FindStringSubmatch(line)
		fields := make(map[string]string)
		for i, name := range r.SubexpNames() {
			if i != 0 {
				fields[name] = match[i]
			}
		}
		lines = append(lines, fields)
	}
	return lines
}

func Max(nums ...int64) int64 {
	if len(nums) == 2 {
		x := nums[0]
		y := nums[1]
		if x > y {
			return x
		}
		return y
	}
	return Max(nums[0], Max(nums[1:]...))
}

func main() {
	dims := MapLines("inputs/2.txt", `(?P<l>\d+)x(?P<w>\d+)x(?P<h>\d+)`)

	var totalArea int64

	for _, d := range dims {
		fmt.Printf("%+v\n", d)

		l, _ := strconv.ParseInt(d["l"], 10, 64)
		w, _ := strconv.ParseInt(d["w"], 10, 64)
		h, _ := strconv.ParseInt(d["h"], 10, 64)

		sa := (2 * l * w) + (2 * w * h) + (2 * h * l)
		ss := l * w * h / Max(w, l, h)
		totalArea += sa
		totalArea += ss
	}
	fmt.Println(totalArea)

}
