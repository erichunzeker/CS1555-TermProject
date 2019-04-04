# insert into schedule (weekday, runtime, route_id, train_id, seats_taken) values ('Wed', time '09:00:00', 1, 1, 0);
w = ['Sun', 'Mon', 'Tues', 'Wed', 'Thur', 'Fri', 'Sat']
r = -1
R = 1
T = 1
S = 0
min = '55'

for i in range(0, 2000):

    if i % 7 == 0 and min == '00':
        min = '05'
    elif i % 7 == 0 and min == '05':
        min = '10'
    elif i % 7 == 0 and min == '10':
        min = '15'
    elif i % 7 == 0 and min == '15':
        min = '20'
    elif i % 7 == 0 and min == '20':
        min = '25'
    elif i % 7 == 0 and min == '25':
        min = '30'
    elif i % 7 == 0 and min == '30':
        min = '35'
    elif i % 7 == 0 and min == '35':
        min = '40'
    elif i % 7 == 0 and min == '40':
        min = '45'
    elif i % 7 == 0 and min == '45':
        min = '50'
    elif i % 7 == 0 and min == '50':
        min = '55'
    elif i % 7 == 0 and min == '55':
        min = '00'
    if i % 84 == 0:
        r += 1
    # if i % 4 == 0:
    #     R += 1
    #
    # if i % 6 == 0:
    #     T += 1



    values = '(\'' + w[i % 7] + '\', time \'' + str(r) + ':' + min + ':00\', ' + str(R) + ', ' + str(T) + ', 0);'

    statement = 'insert into schedule (weekday, runtime, Route_ID, Train_ID, seats_taken) values ' + values
    print(statement)
