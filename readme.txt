scenario1: send message to the same partition number of two topics
>rpk topic create t1 -p 10
>rpk topic create t2 -p 10
>rpk topic consume t1 -g g1
>rpk topic consume t2 -g g1
>rpk group describe g1

>curl --location 'localhost:8080/send?topic=t1&key=2'
>curl --location 'localhost:8080/send?topic=t2&key=2'

result: two messages are sent to partition 8 of t1 and t2


scenario2: let two topics apply the same consumer group re-balance logic
e.g. t1 has 10 partitions, t2 has 10 partitions, consumer instance1 listen t1-1~5, require instance1 listen t2-1~5
if we need the group feature of kafka, we can't achieve above target


