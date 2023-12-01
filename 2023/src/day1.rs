use std::fs;
use std::str::FromStr;

pub fn day1() {
    let _test1 = "1abc2
    pqr3stu8vwx
    a1b2c3d4e5f
    treb7uchet";

    let _contents = &fs::read_to_string("01.txt").expect("wut");

    part1(_contents);
}

pub fn part1(contents: &str) {
    let mut total = 0;
    for line in contents.lines() {
        let numbers: Vec<&str> = line.matches(char::is_numeric).collect();
        let n1 = i32::from_str(numbers.first().unwrap()).unwrap();
        let n2 = i32::from_str(numbers.last().unwrap()).unwrap();
        total += n1 * 10;
        total += n2;
    }
    println!("{total}");
}
