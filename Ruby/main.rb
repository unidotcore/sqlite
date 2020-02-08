require 'sqlite3'

TABLE_NAME = 'users'
DATABASE_FILENAME = 'database.db'

User = Struct.new(:id, :name, :age)

begin
    database = SQLite3::Database.open(DATABASE_FILENAME)
    begin
        query = 'SELECT * FROM %s' % [TABLE_NAME]
        statement_handle = database.prepare(query)
        result = statement_handle.execute
        users = []
        while row = result.next do
            users.push(User.new(row[0], row[1], row[2]))
        end
        puts('%-10s%-15s%-10s' % ['ID', 'NAME', 'AGE'])
        for user in users do
            puts('%-10d%-15s%-10d' % [user.id, user.name, user.age])
        end
    rescue
        puts('Unable to execute query.' + $/)
    ensure
        statement_handle.close if statement_handle
    end
rescue
    puts('Unable to open database.' + $/)
ensure
    database.close if database
end