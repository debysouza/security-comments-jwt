package br.com.api.teste.security.enums;

//esse enum é uma maneira de gerenciar o acesso dos usuários na aplicação
public enum RoleEnum {
	//valores constantes do enum
	ROLE_USER("Role Usuário", 1),
	ROLE_MODERATOR("Role Moderador", 2),
	ROLE_ADMIN("Role Administrador", 3);
	
	//define as variáveis representadas nos argumentos acima
	private String tipo;
    private int codigo;
    
    //construtores parametrizado
    private RoleEnum(String tipo, int codigo) {
        this.tipo = tipo;
        this.codigo = codigo;
    }
    
    private RoleEnum(int codigo) {
        this.codigo = codigo;
    }
    
    //Getters
    public int getCodigo () {
        return codigo;
    }
    
    public String getTipo () {
        return tipo;
    }
    
}