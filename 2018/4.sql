/* create table naps ( */
/*   guard_id int, */
/*   during tsrange */
/* ); */


with
  minutes as (select guard_id,
                     date_part('minute',
                               generate_series(lower(during),
                                               upper(during) - interval '1 minute',
                                              '1 minute')) as minute
              from naps),
  minutes_per_guard as (select guard_id, count(minute) from minutes group by guard_id),
  sleepiest_guard as (select guard_id from minutes_per_guard order by count desc limit 1)
select minute, (minute * (select guard_id from sleepiest_guard)), count(minute)
from minutes
where guard_id = (select * from sleepiest_guard)
group by minute
order by count desc
limit 20
;



/* part 2: which guard is most frequently asleep on the same minute */

with
  minutes as (select guard_id,
                     date_part('minute',
                               generate_series(lower(during),
                                               upper(during) - interval '1 minute',
                                              '1 minute')) as minute
              from naps)
select
  guard_id,
  minute,
  count(minute)
from minutes
group by guard_id, minute
order by count desc
limit 20
;
