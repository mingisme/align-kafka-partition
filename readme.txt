scenario1: send message to the same partition number of two topics
>rpk topic create t1 -p 10
>rpk topic create t2 -p 10
>rpk topic consume t1 -g g1
>rpk topic consume t2 -g g1
>rpk group describe g1

>curl --location 'localhost:8080/send?topic=t1&key=2'
>curl --location 'localhost:8080/send?topic=t2&key=2'

result: two messages are sent to partition 8 of t1 and t2


