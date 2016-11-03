package com.enoqueleal.agenda.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.enoqueleal.agenda.R;
import com.enoqueleal.agenda.dao.AlunoDao;
import com.enoqueleal.agenda.model.Aluno;

import java.util.List;

/**
 * Classe responsável por representar a Activity da lista de Alunos.
 * Created by enoque.santos on 27/10/2016.
 */
public class ListaAlunosActivity extends AppCompatActivity {


    private ListView listaAlunos;

    /**
     * Méotodo responsável pela a inicialização da view lista de alunos.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_alunos);
        
        listaAlunos = (ListView) findViewById(R.id.lista_alunos);

        listaAlunos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View item, int position, long id) {
            Aluno aluno = (Aluno)  listaAlunos.getItemAtPosition(position);
            Intent intentVaiProFormulario = new Intent(ListaAlunosActivity.this, FormularioActivity.class);
                intentVaiProFormulario.putExtra("aluno", aluno);
                startActivity(intentVaiProFormulario);
            }
        });

        Button novoAluno = (Button) findViewById (R.id.novo_aluno);
        novoAluno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Intent intentVaiProFormulario = new Intent(ListaAlunosActivity.this, FormularioActivity.class);
                startActivity(intentVaiProFormulario);
            }
        });

        registerForContextMenu(listaAlunos);
    }

    /**
     * Esse método e executado durante o ciclo de vida da app,
     * e o mesmo esta carregando a lista de alunos para a view.
     */
    @Override
        public void onResume(){
        super.onResume();
        carregaLista();
    }

    /**
     * Método responsável por instanciar um DAO e carregar a lista de alunos.
     */
    private void carregaLista() {
        AlunoDao dao = new AlunoDao(this);
        List<Aluno> alunos = dao.buscaAlunos();
        dao.close();

        ArrayAdapter<Aluno> adapter = new ArrayAdapter<Aluno>(this, android.R.layout.simple_list_item_1, alunos);
        listaAlunos.setAdapter(adapter);
    }

    /**
     * Método responsável por criar um menu de contexto para a lista de alunos
     * @param menu
     * @param v
     * @param menuInfo
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, final ContextMenu.ContextMenuInfo menuInfo)  {
        MenuItem deletar = menu.add("Excluir");
        deletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
                Aluno aluno = (Aluno) listaAlunos.getItemAtPosition(info.position);

                AlunoDao dao = new AlunoDao(ListaAlunosActivity.this);
                dao.deleta(aluno);
                dao.close();

                Toast.makeText(ListaAlunosActivity.this, aluno.getNome() + " foi excluido", Toast.LENGTH_SHORT).show();
                carregaLista();
                return false;
            }
        });
    }
}
