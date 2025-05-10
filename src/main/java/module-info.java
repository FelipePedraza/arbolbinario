module com.arbolbinario {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;

    opens com.arbolbinario to javafx.fxml;
    exports com.arbolbinario;
    exports com.arbolbinario.Model;
    opens com.arbolbinario.Model to javafx.fxml;
    exports com.arbolbinario.Controller;
    opens com.arbolbinario.Controller to javafx.fxml;

    
    
}
