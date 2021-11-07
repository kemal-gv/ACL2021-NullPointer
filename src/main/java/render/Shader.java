package render;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.*;

public class Shader {
    private int program;
    private int verticeShader;//Vertice shader
    private int fragmentShader;//Fragment shader
    private FloatBuffer floatBuffer;

    public Shader(String filename){
        floatBuffer=BufferUtils.createFloatBuffer(16);

        program=glCreateProgram();
        verticeShader = glCreateShader(GL_VERTEX_SHADER);

        glShaderSource(verticeShader,readFile(filename+".vs"));
        //COmpile the shader
        glCompileShader(verticeShader);
        if(glGetShaderi(verticeShader,GL_COMPILE_STATUS) != 1){
            //Erreur ds la compilation du shader
            System.err.println(glGetShaderInfoLog(verticeShader));
            System.exit(1);
        }


        fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);

        glShaderSource(fragmentShader,readFile(filename+".fs"));
        //COmpile the shader
        glCompileShader(fragmentShader);
        if(glGetShaderi(fragmentShader,GL_COMPILE_STATUS) != 1){
            //Erreur ds la compilation du shader
            System.err.println(glGetShaderInfoLog(fragmentShader));
            System.exit(1);
        }

        glAttachShader(program,verticeShader);
        glAttachShader(program,fragmentShader);

        glBindAttribLocation(program,0,"vertices");
        glBindAttribLocation(program,1,"textures");


        glLinkProgram(program);
        if(glGetProgrami(program,GL_LINK_STATUS) != 1){
            System.err.println(glGetShaderInfoLog(fragmentShader));
            System.exit(1);
        }
        glValidateProgram(program);
        if(glGetProgrami(program,GL_VALIDATE_STATUS) != 1){
            System.err.println(glGetShaderInfoLog(fragmentShader));
            System.exit(1);
        }



    }

    public void bind(){
        glUseProgram(program);
    }

    private String readFile(String filename){
        StringBuilder strB=new StringBuilder();
        BufferedReader br;
        try{
            String url= Shader.class.getClassLoader().getResource("shaders/"+filename).getFile();
            br=new BufferedReader(new FileReader(new File(url)));
            String line;
            while((line=br.readLine()) != null ){
                strB.append(line);
                strB.append("\n");

            }

            br.close();

        }catch(IOException e){
            e.printStackTrace();
        }


        return strB.toString();
    }

    public void setUniform(String name, Matrix4f value){
        int location = glGetUniformLocation(program,name);
        FloatBuffer buffer=floatBuffer;//= BufferUtils.createFloatBuffer(16);//4x4 of data (a matrix)
        value.get(buffer);


        if(location != -1){
            glUniformMatrix4fv(location,false,buffer);
        }
    }

    public void setUniform(String uniformName, int value) {
        int location = glGetUniformLocation(program, uniformName);
        if (location != -1) glUniform1i(location, value);
    }
}

