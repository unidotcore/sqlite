import sys

if sys.version_info[0] < 3:
    raise Exception('Please, use Python 3.')

TABLE_NAME = 'users'
DATABASE_FILENAME = 'database.db'


class User:

    def __init__(self, user):
        self.id, self.name, self.age = user


def main():
    
    import sqlite3

    connection = sqlite3.connect(DATABASE_FILENAME)
    if not connection:
        print('Unable to open database.')
        return 1
    cursor = connection.cursor()
    query = 'SELECT * FROM {}'.format(TABLE_NAME)
    try:
        result = cursor.execute(query)
    except:
        print('Unable to execute query.')
        return 1
    users = map(lambda user: User(user), result)
    print('{0:<10}{1:<15}{2:<10}'.format('ID', 'NAME', 'AGE'))
    for user in users:
        print('{0:<10}{1:<15}{2:<10}'.format(user.id, user.name, user.age))
    connection.close()
    return 0


if __name__ == '__main__':
    sys.exit(main())