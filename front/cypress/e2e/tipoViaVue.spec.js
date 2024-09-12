describe('TipoViaView Functionality Tests', () => {
    beforeEach(() => {
        cy.intercept('GET', '/api/tipovia', {
            fixture: 'tipos.json',
        }).as('getTipos');

        cy.visit('/');
    });

    it('cargar la pagina', () => {
        cy.get('a[href="/tipo-vias"]').click();

        cy.wait('@getTipos');

        cy.contains('Tipos de Vía');
        cy.get('table.tipo-via-table').should('be.visible');
    });

    it('crear nuevo tipo de vía', () => {
        cy.intercept('POST', '/api/tipovia', {
            statusCode: 200,
            body: {
                id: 3,
                tipo: 'Nuevo Tipo',
                codigo: 'NT',
                abreviatura: 'NT',
                descripcion: 'Descripción del nuevo tipo',
            },
        }).as('createTipoVia');

        cy.get('a[href="/tipo-vias"]').click();

        cy.wait('@getTipos');

        cy.get('input[id="tipo"]').type('Nuevo Tipo');
        cy.get('input[id="codigo"]').type('NT');
        cy.get('input[id="abreviatura"]').type('NT');
        cy.get('textarea[id="descripcion"]').type('Descripción del nuevo tipo');
        cy.get('form').first().submit();
    });

    it('editar tipo de vía', () => {
        cy.intercept('PUT', '/api/tipovia/1', {
            statusCode: 200,
            body: {
                id: 1,
                tipo: 'Tipo Editado',
                codigo: 'TE',
                abreviatura: 'TE',
                descripcion: 'Descripción editada',
            },
        }).as('editTipoVia');

        cy.get('a[href="/tipo-vias"]').click();

        cy.wait('@getTipos');

        cy.get('.btn-edit').first().click();

    });

    it('eliminar tipo de vía', () => {
        cy.intercept('DELETE', '/api/tipovia/1', {
            statusCode: 200,
        }).as('deleteTipoVia');

        cy.get('a[href="/tipo-vias"]').click();

        cy.wait('@getTipos');

        cy.get('.btn-delete').first().click();
        cy.get('.modal-actions button').contains('Sí, eliminar').click();
    });

    it('mensage de error tipo de vía', () => {
        cy.intercept('POST', '/api/tipovia', {
            statusCode: 500,
        }).as('createTipoViaFail');

        cy.get('a[href="/tipo-vias"]').click();

        cy.wait('@getTipos');

        cy.get('input[id="tipo"]').type('Nuevo Tipo');
        cy.get('input[id="codigo"]').type('NT');
        cy.get('input[id="abreviatura"]').type('NT');
        cy.get('textarea[id="descripcion"]').type('Descripción del nuevo tipo');
        cy.get('form').first().submit();
    });
});