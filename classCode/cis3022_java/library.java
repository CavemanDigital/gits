import java.util.Scanner;
class Book{
	private String Title;
	private String Author[];
	private int numChapters;
	
	public Book(){
		Title= new String();
		Author= new String[2];
		numChapters= 0;
	}
	public Book(String Title, String Author[], int numChapters){
		this.Title=Title;
		this.Author=Author;
		this.numChapters=numChapters;;
	}
	public String getTitle(){
		return Title;
	}
	public void setTitle(String Title){
		this.Title=Title;
	}
	public String[] getAuthor(){
		return Author;
	}
	public void setAuthor(String[] Author){
		this.Author=Author;
	}
	public int setNumChapters(){
		return numChapters;
	}
	public void setNumChapters(int numChapters){
		this.numChapters=numChapters;
	}
	public String toString(){
		String strAuthor=new String();;
		String strTitle= "Title: " + Title + "\n";
		for(int i=0; i<Author.length; i++){
			strAuthor=strAuthor + Author[i] + ". ";
		}
		String str="Authors: " + strAuthor + "\n";
		String strInt= "The book has " + numChapters + " chapters\n-----------\n";
		return strTitle+str+strInt;
	}
}
class Library{
	private Book[] books;
	
	public Library(){
		books = new Book[10];
	}
	public Library(Book[] books){
		this.books=books;
	}
	public Book[] getBooks(){
		return books;
	}
	public void setBooks(Book[] books){
		this.books=books;
	}
	public void addBook(Book book){
		int index=0;
		if(books[index]!=null){
			index++;
		}
		else if(books[9]!=null){
			System.out.println("Your Library Is Full!!!");
		}
		else{
			this.books[index]=book;
		}
	}
	public String toString(){
		String strBook=new String();
		for(int i=0; i<books.length; i++){
			strBook=strBook + books[i];
		}
		return "Books in library: \n" + strBook;
	}
}
class LibraryDriver{
	public static void main(String args[]){
	//Add Books
	Library library = new Library();
	String Author[]= {"me","me1"};
	library.addBook(new Book("Book1", Author, 15));
	library.addBook(new Book("Book2", Author, 15));
	library.addBook(new Book("Book3", Author, 15));
	//Show All Books
	System.out.println(library.toString());
	}
}