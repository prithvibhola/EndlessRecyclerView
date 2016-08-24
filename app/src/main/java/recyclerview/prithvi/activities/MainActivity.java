package recyclerview.prithvi.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import recyclerview.prithvi.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button buttonLinear;
    Button buttonGrid;
    Button buttonStaggered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonLinear = (Button) findViewById(R.id.bLinear);
        buttonGrid = (Button) findViewById(R.id.bGrid);
        buttonStaggered = (Button) findViewById(R.id.bStaggered);

        buttonLinear.setOnClickListener(this);
        buttonGrid.setOnClickListener(this);
        buttonStaggered.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bLinear:
                startActivity(new Intent(MainActivity.this, LinearActivity.class));
                break;
            case R.id.bGrid:
                startActivity(new Intent(MainActivity.this, GridActivity.class));
                break;
            case R.id.bStaggered:
                startActivity(new Intent(MainActivity.this, StaggeredActivity.class));
                break;
        }
    }
}
