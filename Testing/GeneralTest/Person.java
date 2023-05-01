package Testing.GeneralTest;
public class Person {

    public String name;
    public String email;
    public int age;

    public Person(String name, String email, int age){
        this.name=name;
        this.email=email;
        this.age=age;
    }

    public void setName( String newName){
        name=newName;
        return;
    }

    public void setEmail( String newEmail){
        email=newEmail;
        return;
    }

    public void setAge( int newAge){
        age=newAge;
        return;
    }

    public void printName(){
        System.out.println("Name is : "+name);
        return;
    }

    public void printEmail(){
        System.out.println("Email is : "+email);
        return;
    }

    public void printAge(){
        System.out.println("Age is : "+age);
        return;
    }

    public void printGender() throws Exception{
        throw new Exception();
    }

    public bool isAdult(){
        if(age>=18){
            return true;
        }
        return false;
    }

    public bool isTeen(){
        if(age>=13 && age<=19){
            return true;
        }
        return false;
    }
    
    public void PrintInfo() {
        printName();
        printEmail();
        printAge();
        try{
            printGender();
        }
        catch(Exception e){
            System.out.println("Exception caught in Running Gender");
        }
        return;
    }

    public void test(){
        PrintInfo();
        
    }
}
