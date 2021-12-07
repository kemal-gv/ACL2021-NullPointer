package models;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

public class Model {
    private int drawCount;
    private int verticeID;
    private int textureID;//Texture id
    private int indiceID;//Indice

    public Model(float[] vertices, float[] texCoords, int[] indices) {
        drawCount = indices.length;


        verticeID = glGenBuffers();//Generation de l'id
        glBindBuffer(GL_ARRAY_BUFFER, verticeID);//On relie l'id au tableau ?
        //On passe la data dans le buffer
        glBufferData(GL_ARRAY_BUFFER, createBuffer(vertices), GL_STATIC_DRAW);

        //Texture id dans la carte grapgique mais on l'utilise pas encore
        textureID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, textureID);
        glBufferData(GL_ARRAY_BUFFER, createBuffer(texCoords), GL_STATIC_DRAW);


        indiceID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indiceID);
        IntBuffer buffer = BufferUtils.createIntBuffer(indices.length);
        buffer.put(indices);
        buffer.flip();
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);


        glBindBuffer(GL_ARRAY_BUFFER, 0);//On unbind pr que rien ne l'affecte


        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);


    }

    public void render() {
        // glEnableClientState(GL_VERTEX_ARRAY);
        //glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glBindBuffer(GL_ARRAY_BUFFER, verticeID);

        //On lui dit quoi faire de la valeur ds le buffer
        //glVertexPointer(3,GL_FLOAT,0,0);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);


        glBindBuffer(GL_ARRAY_BUFFER, textureID);
        //On lui dit quoi faire de la valeur ds le buffer
        //glTexCoordPointer(2,GL_FLOAT,0,0);//2 car on a que 2 points pr les coords de la texture
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);


        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indiceID);
        glDrawElements(GL_TRIANGLES, drawCount, GL_UNSIGNED_INT, 0);


        glDrawArrays(GL_TRIANGLES, 0, drawCount);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        //  glDisableClientState(GL_VERTEX_ARRAY);
        // glDisableClientState(GL_TEXTURE_COORD_ARRAY);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);


    }

    public void rotate() {

    }

    private FloatBuffer createBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();

        return buffer;
    }
}
