package com.example.mad

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mad.model.Statement
import com.example.mad.ui.theme.MADTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MADTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("Android")
                    ScreenContent( LocalContext.current )
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
private fun ScreenContent( context: Context ) {
    // ArrayList to hold the predefined quiz statements. Also, initialize it.
    val quizStatements: MutableList<Statement> = remember { mutableStateListOf() }
    quizStatements.addAll(generateStatements())

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        style = MaterialTheme.typography.h6,
                        color = MaterialTheme.colors.onPrimary
                    )
                },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onPrimary
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                QuizInstructionsHeader()
                QuizStatements(
                    quizStatements,
                    removeQuizStatement = { statement: Statement ->
                        quizStatements.remove(statement)
                    },
                    context = context
                )
            }
        }
    )

}

@Composable
fun QuizInstructionsHeader() {
        Text(
            text = stringResource(id = R.string.quiz_instr_header),
            style = MaterialTheme.typography.h6,
            color = MaterialTheme.colors.onBackground
        )
        Text(
            text = stringResource(id = R.string.quiz_instr_description),
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onBackground
        )

}

private fun generateStatements(): ArrayList<Statement> {
    return arrayListOf(
        Statement("A \'val\' and \'var\' are the same.", false),
        Statement("Mobile Application Development grants 12 ECTS.", false),
        Statement("A unit in Kotlin corresponds to a void in Java.", true),
        Statement("In Kotlin \'when\' replaces the \'switch\' operator in Java.", true)
    )
}

@Composable
private fun QuizStatements(localQuizStatements: MutableList<Statement>,
                           removeQuizStatement: (Statement) -> Unit,
                           context: Context) {
    LazyColumn(modifier = Modifier.fillMaxHeight()) {
        items(items = localQuizStatements, itemContent = { quizStatement ->
            Row(modifier = Modifier
                .fillMaxWidth()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = { /* Called when the gesture starts */ },
                        onDoubleTap = { /* Called on Double Tap */ },
                        onLongPress = {
                                      //check if statement is false and show a Toast message and remove the statement
                                        if (quizStatement.isTrue.not()) {
                                            informUser(context = context  , msgId = R.string.answer_is_false)
                                            removeQuizStatement(quizStatement)
                                        } else {
                                            informUser(context = context , msgId = R.string.wrong_answer)
                                        }


                        },
                        onTap = {
                            //check if statement is true and show a Toast message and remove the statement
                            if (quizStatement.isTrue) {
                                informUser(context = context , msgId = R.string.answer_is_true)
                                removeQuizStatement(quizStatement)
                            } else {
                                informUser(context = context , msgId = R.string.wrong_answer)
                            }
                        }
                    )
                }
            ) {
                Text(
                    modifier = Modifier
                        .padding(16.dp),
                    text = quizStatement.statement,
                )
            }
            Divider(
                color = Color.LightGray, modifier = Modifier.alpha(0.6f),
                thickness = 1.dp
            )
        })
    }
}


private fun informUser(context: Context, msgId: Int) {
    Toast.makeText(context, context.getString(msgId), Toast.LENGTH_SHORT).show()
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MADTheme {
        Greeting("Android")
        ScreenContent( context = LocalContext.current)
    }
}