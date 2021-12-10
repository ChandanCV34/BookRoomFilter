package com.example.bookfilterusingroom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val authorInput = findViewById<TextInputLayout>(R.id.AuthorInput)

        val dataCount = findViewById<TextView>(R.id.resultOne)
        val dataResultTwo = findViewById<TextView>(R.id.resulttwo)

        val filterButton = findViewById<Button>(R.id.button)
        val titles = mutableListOf<Bookdata>()
        val myApplication=application as MyApplication
        val authlist=myApplication.httpApiService
        val db=AppDatabase.getDatabase(this)
        filterButton.setOnClickListener {
            titles.clear()
            dataCount.text = ""
            dataResultTwo.text = ""
            var c: Int
            dataResultTwo.text=""
            CoroutineScope(Dispatchers.IO).launch {

                var result = authlist.getMyBookData()
                for (i in result)
                    titles.add(i)
            }
            GlobalScope.launch {
                var auth:Int=0
                for (item in titles) {
                    titles.add(item)
                    c = 0
                    val AuthursList: List<Authors> = db.authorDao().getAll()
                    //var aa: AuthorDetails = AuthorDetails(author = item.author, country = item.country)
                    for (items in AuthursList) {
                        if (items.author.lowercase() == item.author.lowercase()) {
                            auth = items.Aid
                            c = 1
                            break
                        }
                    }
                    if (c == 1) {
                        db.BookDao()
                            .InsertBooks(
                                Book(
                                    aid = auth,
                                    language = item.language,
                                    imageLink = item.imageLink,
                                    link = item.link,
                                    pages = item.pages,
                                    title = item.title,
                                    year = item.year
                                )
                            )
                    } else {
                        db.authorDao().insert(
                            Authors(
                                author = item.author,
                                country = item.country
                            )
                        )

                        auth = db.authorDao()
                            .getAuhtor(item.author).Aid
                        db.BookDao()
                            .InsertBooks(
                                Book(
                                    aid = auth,
                                    language = item.language,
                                    imageLink = item.imageLink,
                                    link = item.link,
                                    pages = item.pages,
                                    title = item.title,
                                    year = item.year
                                )
                            )
                    }
                }
                val list:List<AuthorsandBooks> = db.authorDao().JoinedDetails(authorInput.editText?.text?.toString()?.lowercase())
                withContext(Dispatchers.Main) {

                    var count:Int=0
                    var res = ""

                    if(list.size>=1){
                        res+="Result: ${list[0].title} (${list[0].BookID})\n"
                        count+=1
                    }
                    if(list.size>=2){
                        res+="Result: ${list[1].title} (${list[1].BookID})\n"
                        count+=1
                    }
                    if(list.size>=3){
                        res+="Result: ${list[2].title} (${list[2].BookID})\n"
                        count+=1
                    }
                    dataCount.text="Result: $count"
                    dataResultTwo.text = res
                }
            }
        }

    }
}