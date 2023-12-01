fn main() {
    println!("hi there");

    let contents = fs::read_to_string("01.txt").expect("wut");

    println!(contents);
}
