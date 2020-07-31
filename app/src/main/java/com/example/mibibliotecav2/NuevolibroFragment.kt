package com.example.mibibliotecav2
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mibibliotecav2.model.remote.LibrosRemote
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.fragment_nuevolibro.*
import java.io.ByteArrayOutputStream


class NuevolibroFragment : Fragment() {
    private var fotosList: MutableList<String> = mutableListOf()
    private var notasList: MutableList<String> = mutableListOf()
    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val myRef = database.getReference("libros")
    var idl = ""
    var idlibro = myRef.push().key

    private val REQUEST_IMAGE_ISBN = 5
    private val REQUEST_IMAGE_ISBN1 = 21
    private val REQUEST_IMAGE_PORTADA = 6
    private val REQUEST_IMAGE_PORTADA1 = 20
    private val REQUEST_IMAGE_GALERIA = 7
    private val REQUEST_IMAGE_GALERIA1 = 22
    private var urlisbn = ""
    private var urlportada = ""
    val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val user = mAuth.currentUser
    val usrid = user?.uid

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_nuevolibro, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {

            val safeArgs = NuevolibroFragmentArgs.fromBundle(it)
            idl = safeArgs.idedit

            if (idl != "ok") {
                val libro = safeArgs.libro

                et_titulo.setText(libro.titulo)
                et_autor.setText(libro.autor)
                et_edicion.setText(libro.nedicion)
                et_lugarpublicacion.setText(libro.lpublicacion)
                et_anoedicion.setText(libro.aedicion)
                et_numpaginas.setText(libro.npag)
                et_genero.setText(libro.genero)
                urlisbn = libro.portada
                urlisbn = libro.isbn
                fotosList = libro.galeria
                notasList = libro.notas
                idlibro = idl

            }


        }







        bt_examinarisbn.setOnClickListener {
            escoger(REQUEST_IMAGE_ISBN, REQUEST_IMAGE_ISBN1)

        }
        bt_examinarportada.setOnClickListener {
            escoger(REQUEST_IMAGE_PORTADA, REQUEST_IMAGE_PORTADA1)
        }
        bt_notas.setOnClickListener {
            notasList.add(et_notas.text.toString())
            et_notas.setText("")
        }
        bt_examinargaleria.setOnClickListener {
            escoger(REQUEST_IMAGE_GALERIA, REQUEST_IMAGE_GALERIA1)

        }



        bt_guardar.setOnClickListener {
            val titulo = et_titulo.text.toString()
            val autor = et_autor.text.toString()
            val edicion = et_edicion.text.toString()
            val lugar = et_lugarpublicacion.text.toString()
            val year = et_anoedicion.text.toString()
            val paginas = et_numpaginas.text.toString()
            val genero = et_genero.text.toString()




                    if (titulo.isNotEmpty() && genero.isNotEmpty() && autor.isNotEmpty() && paginas.isNotEmpty()) {


                        val libro: LibrosRemote = LibrosRemote(
                            idlibro,
                            titulo,
                            autor,
                            edicion,
                            lugar,
                            year,
                            paginas,
                            genero,
                            notasList,
                            urlisbn,
                            urlportada,
                            fotosList
                        )
                        myRef.child(usrid.toString()).child(idlibro!!).setValue(libro)
                        findNavController().navigate(R.id.action_nuevolibroFragment_to_bibliotecaPersonalFragment)
                    }


                    else

                    {
                        Toast.makeText(requireContext(), "llene todos los campos obligatorios", Toast.LENGTH_SHORT).show()
                        if (titulo.isEmpty())
                            et_titulo.error = "Este campo es obligatorio"
                        if (genero.isEmpty())
                            et_genero.error = "Este campo es obligatorio"
                        if (autor.isEmpty())
                            et_autor.error = "Este campo es obligatorio"
                        if (paginas.isEmpty())
                            et_numpaginas.error = "Este campo es obligatorio"
                }


        }


    }

    private fun escoger(requestImage: Int, requestImage1: Int) {
        val builder = AlertDialog.Builder(activity)
        val items = arrayOfNulls<CharSequence>(2)
        items[0] = "Cámara"
        items[1] = "Galería"
        builder.setTitle("Seleccione una foto")
            .setItems(items) { dialog, which ->
                when (which) {
                    0 -> dispatchTakePictureIntent(requestImage)
                    1 -> cargarImagen(requestImage1)
                }
            }
        builder.create()
        builder.show()
    }


    private fun cargarImagen(requestImagePortada: Int) {
        Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        ).also { takePictureIntent ->
            takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                startActivityForResult(takePictureIntent, requestImagePortada)
            }

        }

    }


    private fun dispatchTakePictureIntent(requestImage: Int) {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                startActivityForResult(takePictureIntent, requestImage)
            }

        }
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(  resultCode == Activity.RESULT_OK) {
            Toast.makeText(requireContext(), "Espere un momento", Toast.LENGTH_LONG).show()
            var uploadTask: UploadTask
            val mStorage = FirebaseStorage.getInstance()
            val id = myRef.push().key
            val photoRef = mStorage.reference.child(idlibro!!).child(id!!)
            if (requestCode > 10) {
                val uri: Uri? = data?.data
                uploadTask = photoRef.putFile(uri!!)

            } else {
                var bitmap = data?.extras?.get("data") as Bitmap
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val data = baos.toByteArray()

                uploadTask = photoRef.putBytes(data)
            }

            val urlTask = uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                photoRef.downloadUrl }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (requestCode == REQUEST_IMAGE_ISBN || requestCode == REQUEST_IMAGE_ISBN1) {
                        urlisbn = task.result.toString()
                        bt_examinarisbn.setImageResource(R.drawable.ic_fotocargada)
                    } else if (requestCode == REQUEST_IMAGE_PORTADA1 || requestCode == REQUEST_IMAGE_PORTADA) {
                        urlportada = task.result.toString()
                        bt_examinarportada.setImageResource(R.drawable.ic_fotocargada)
                    } else {
                        fotosList.add(task.result.toString())
                        Toast.makeText(requireContext(), "Imagen Agregada", Toast.LENGTH_SHORT)
                            .show()


                    }
                }
            }


        }

    }

}