import psycopg2
from psycopg2.extras import DateTimeRange
import re

WROTE = 0


def main():
    with psycopg2.connect("dbname=aoc") as conn:
        with conn.cursor() as cur:
            with open("inputs/4.txt") as f:
                guard_id = 0
                nap_start = None
                nap_end = None
                for line in f:
                    m = re.search("\[(.+)\]", line)
                    ts = m.group(1)

                    m = re.search(" #(\d+) ", line)
                    # begins shift
                    if m:
                        # if guard_id and nap_start and not nap_end:
                        #     write_nap(guard_id, nap_start, end_of_hour(nap_end))
                        guard_id = m.group(1)

                    if "falls" in line:
                        nap_start = ts

                    if "wakes" in line:
                        nap_end = ts

                    if guard_id and nap_start and nap_end:
                        write_nap(cur, guard_id, nap_start, nap_end)
                        nap_start = nap_end = None

    print(WROTE)


def write_nap(cur, guard_id, nap_start, nap_end):
    during = DateTimeRange(lower=nap_start, upper=nap_end, bounds="[)")
    cur.execute(
        """
        insert into naps
        (guard_id, during)
        values
        (%s, %s)
        """,
        (guard_id, during),
    )
    global WROTE
    WROTE += 1


main()
