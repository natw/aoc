package main

import (
	"fmt"
	"strings"
)

func main() {
	a := "012((hi)7)8"
	b := strings.LastIndex(a, "(")
	e := strings.Index(a, ")")
	fmt.Println(a[b : e+1])
	a = a[:b] + "poop" + a[e+1:]
	fmt.Println(a)
}
