# insert into schedule (weekday, runtime, route_id, train_id, seats_taken) values ('Wed', time '09:00:00', 1, 1, 0);
w = ['Mon', 'Tues', 'Wed', 'Thur', 'Fri']
r = 8
R = 1
T = 1
S = 0
min = '00'

for i in range(0, 1999):

    if i % 200 == 0:
        r += 1
    if i % 100 == 0 and min is '00':
        min = '30'
    if i % 100 == 0 and min is '30':
        min = '00'

    if i % 4 == 0:
        R += 1

    if i % 6 == 0:
        T += 1



    values = '(\'' + w[i % 5] + '\', time \'' + str(r) + ':' + min + ':00\', ' + str(R) + ', ' + str(T) + ', 0);'

    statement = 'insert into schedule (weekday, runtime, Route_ID, Train_ID, seats_taken) values ' + values
    print(statement)
