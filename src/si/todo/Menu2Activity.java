package si.todo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class Menu2Activity extends Activity implements OnClickListener {
	
	Button addTodo = null;
	Button editTodo = null;
	Button showTodos = null;
	Button removeTodo = null;
	
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu2);
        
        addTodo = (Button)findViewById(R.id.button_add_todo);
        addTodo.setOnClickListener(this);
        
        editTodo = (Button)findViewById(R.id.button_edit_todo);
        editTodo.setOnClickListener(this);
        
        showTodos = (Button)findViewById(R.id.button_show_todos);
        showTodos.setOnClickListener(this);
        
        removeTodo = (Button)findViewById(R.id.button_remove_todo);
        removeTodo.setOnClickListener(this);

    }

	@Override
	public void onClick(View v) {
		if(v == showTodos){
			startActivityForResult(new Intent(this, ShowTodosActivity.class), 1);
		} else if (v == addTodo) {
			startActivityForResult(new Intent(this, AddTodoActivity.class), 2);
		} else if (v == editTodo) {
			
		} else if (v == removeTodo) {
			startActivityForResult(new Intent(this, RemoveTodoActivity.class), 4);
		}
			
	} // end of onClick method
	
}

