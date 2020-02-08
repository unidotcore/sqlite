use DBI;
use strict;
use Class::Struct;

use constant DRIVER => "SQLite";
use constant TABLE_NAME => "users";
use constant DATABASE_FILENAME => "database.db";
use constant DATABASE_URL => sprintf("DBI:%s:dbname=%s", DRIVER, DATABASE_FILENAME);

struct User => {
    id => '$',
    name => '$',
    age => '$'
};

my $database = DBI->connect
(
    DATABASE_URL,
    '',
    '',
    { RaiseError => 1, AutoCommit => 0 }
) or die "Unable to open database.\n";

my $query = sprintf("SELECT * FROM %s", TABLE_NAME);
my $statement_handle = $database->prepare($query);
$statement_handle->execute() or die "Unable to execute query.\n";

my @users = ();
while (my @row = $statement_handle->fetchrow_array()) {
    my $user = User->new();
    $user->id(@row[0]);
    $user->name(@row[1]);
    $user->age(@row[2]);
    push(@users, $user);
}

print sprintf("%-10s%-15s%-10s\n", 'ID', 'NAME', 'AGE');
foreach my $user (@users) {
    print sprintf("%-10d%-15s%-10d\n", $user->id, $user->name, $user->age);
}

$database->disconnect();