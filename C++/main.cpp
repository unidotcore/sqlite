#include <string>
#include <vector>
#include <iostream>
#include <sqlite3.h>

#define TABLE_NAME 			"users"
#define DATABASE_FILENAME 	"database.db"

using namespace std;

typedef struct {
	string name;
	unsigned int id, age;
} User;

int callback(void *param, int columns_count, char **values, char **columns_names) {
	vector<User> *users = reinterpret_cast<vector<User> *>(param);
	User user = User();
	user.id = atoi(values[0]);
	user.name = static_cast<string>(values[1]);
	user.age = atoi(values[2]);
	users->push_back(user);
	return 0;
}

int main() {
	sqlite3 *db;
	vector<User> users;
	if (sqlite3_open(DATABASE_FILENAME, &db) != 0) {
		cout << "Unable to open database." << endl;
		return 1;
	}
	char query[100];
	sprintf(query, "SELECT * FROM %s", TABLE_NAME);
	if (sqlite3_exec(db, query, callback, &users, nullptr) != 0) {
		cout << "Unable to execute query." << endl;
		return 1;
	}
	printf("%-10s%-15s%-10s\n", "ID", "NAME", "AGE");
	for (vector<User>::iterator user = users.begin(); user != users.end(); user++) {
		printf("%-10d%-15s%-10d\n", user->id, user->name.c_str(), user->age);
	}
	sqlite3_close(db);
	return 0;
}