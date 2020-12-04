package main

import (
	"crypto/md5"
	"fmt"
)

func main() {
	input := []byte("ckczppom")
	part2(input)
}

func part2(input []byte) {

	i := 0
	for {
		is := []byte(fmt.Sprintf("%d", i))
		is2 := append(input, is...)
		hash := fmt.Sprintf("%x", md5.Sum(is2))
		if string(hash[:6]) == "000000" {
			fmt.Println(i)
			return
		}
		i++
	}

}
func part1(input []byte) {

	i := 0
	for {
		is := []byte(fmt.Sprintf("%d", i))
		is2 := append(input, is...)
		hash := fmt.Sprintf("%x", md5.Sum(is2))
		fmt.Println(string(hash[:5]))
		if string(hash[:5]) == "00000" {
			fmt.Println(i)
			return
		}
		i++
	}

}
