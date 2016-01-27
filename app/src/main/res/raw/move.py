#!/usr/bin/python

import sqlite3
conn = sqlite3.connect('content.db')
print "Opened database successfully";

cur = conn.cursor()
for i in range(234, 238):
    var_query2 = "insert into exercise_full (Id, Lesson, LessonSection, LessonSignature, LessonExample, LessonContent, LessonSubContent) values (NULL ,(select Lesson from exercise where Id = " + str(i)  + ") ,(select Section from exercise where Id = " + str(i)  + ") ,(select Signature from exercise where Id = " + str(i)  + ") ,(select Example from exercise where Id = " + str(i)  + ") ,(select Content  from exercise where Id = " + str(i)  + ") ,(select SubContent from exercise where Id = " + str(i)  + "));"
    print(var_query2)
    cur.execute(var_query2)
    conn.commit()
conn.close()

