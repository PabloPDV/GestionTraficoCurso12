describe('ViaView Functionality Tests', () => {
    beforeEach(() => {
        cy.intercept('GET', '/api/vias', {
            fixture: 'vias.json',
        }).as('getVias');

        cy.intercept('GET', '/api/tipovia', {
            fixture: 'tipos.json',
        }).as('getTipos');

        cy.visit('/');
    });

    it('fetches vias', () => {
        cy.get('a[href="/vias"]').click();

        cy.wait('@getVias');

        cy.get('tbody tr').should('have.length', 2);
        cy.contains('Via 1');
        cy.contains('Via 2');
    });

    it('crear via', () => {
        cy.intercept('POST', '/api/vias', {
            statusCode: 200,
            body: {
                id: 3,
                nombre: 'Via 3',
                estado: 'Regular',
                congestion: 'MEDIO',
                tipo: { id: 1 },
                longitud: 150,
                ancho: 15,
                capacidad: 1500,
            },
        }).as('createVia');

        cy.intercept('GET', '/api/vias', {
            fixture: 'vias.json',
        }).as('getVias');

        cy.get('a[href="/vias"]').click();

        cy.wait('@getVias');

        cy.get('input[id="nombre"]').type('Via 3');
        cy.get('select[id="tipo"]').select('1');
        cy.get('input[id="estado"]').type('Regular');
        cy.get('select[id="congestion"]').select('MEDIO');
        cy.get('input[id="longitud"]').type('150');
        cy.get('input[id="ancho"]').type('15');
        cy.get('input[id="capacidad"]').type('1500');
        cy.get('form').submit();

        cy.wait('@createVia');

        cy.intercept('GET', '/api/vias', {
            statusCode: 200,
            body: [
                {
                    id: 1,
                    nombre: 'Via 1',
                    estado: 'Bueno',
                    congestion: 'BAJO',
                    tipo: { id: 1 },
                    longitud: 100,
                    ancho: 10,
                    capacidad: 1000,
                },
                {
                    id: 2,
                    nombre: 'Via 2',
                    estado: 'Malo',
                    congestion: 'ALTO',
                    tipo: { id: 2 },
                    longitud: 200,
                    ancho: 20,
                    capacidad: 2000,
                },
                {
                    id: 3,
                    nombre: 'Via 3',
                    estado: 'Regular',
                    congestion: 'MEDIO',
                    tipo: { id: 1 },
                    longitud: 150,
                    ancho: 15,
                    capacidad: 1500,
                },
            ],
        }).as('getUpdatedVias');
    });

    it('editar via', () => {
        cy.intercept('PUT', '/api/vias/1', {
            statusCode: 200,
        }).as('editVia');

        cy.get('a[href="/vias"]').click();

        cy.wait('@getVias');

        cy.get('.btn-edit').first().click();

        cy.get('form').first().within(() => {
            cy.root().submit();
        });
    });

    it('deletes an existing via', () => {
        cy.intercept('DELETE', '/api/vias/1', {
            statusCode: 200,
        }).as('deleteVia');

        cy.get('a[href="/vias"]').click();

        cy.wait('@getVias');

        cy.get('.btn-delete').first().click();
        cy.get('.modal-actions button').contains('Sí, eliminar').click();

        cy.wait('@deleteVia');
    });
});