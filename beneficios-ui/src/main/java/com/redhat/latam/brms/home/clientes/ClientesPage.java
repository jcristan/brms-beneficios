package com.redhat.latam.brms.home.clientes;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;

import com.redhat.latam.brms.BasePage;
import com.redhat.latam.brms.BeneficiosSession;
import com.redhat.latam.brms.config.ConfigurationPage;
import com.redhat.latam.brms.home.clientes.nuevo.NuevoClientePage;
import com.redhat.latam.brms.model.Cliente;
import com.redhat.latam.brms.repository.Repository;

public class ClientesPage extends BasePage {

	private static final long serialVersionUID = -7014493250122877814L;

	public ClientesPage() {

		PropertyListView<Cliente> table = this.createTable();
		Link<BasePage> nuevoLink = this.createNuevoLink();
		add(table);
		add(nuevoLink);
	}

	private Link<BasePage> createNuevoLink() {

		Link<BasePage> link = new Link<BasePage>("nuevo") {

			@Override
			public void onClick() {

				setResponsePage(NuevoClientePage.class);

			}

		};
		return link;
	}

	private Link<BasePage> createDeleteAction(final Cliente cliente) {

		return new Link<BasePage>("delete") {

			@Override
			public void onClick() {

				Repository.getInstance().delete(cliente);
				setResponsePage(ClientesPage.class);
			}
		};
	}

	private Link<BasePage> createLoginLink(final Cliente cliente) {

		return new Link<BasePage>("login") {

			@Override
			public void onClick() {

				((BeneficiosSession) getSession()).setCliente(cliente);
				setResponsePage(ClientesPage.class);
			}
		};
	}

	private Link<BasePage> createLogoutLink(final Cliente cliente) {

		return new Link<BasePage>("logout") {

			@Override
			public void onClick() {

				BeneficiosSession session = (BeneficiosSession) getSession();
				if (cliente.equals(session.getCliente())) session.setCliente(null);
				else throw new RuntimeException("El cliente no esta loggeado");
				setResponsePage(ClientesPage.class);
			}
		};
	}

	public PropertyListView<Cliente> createTable() {

		List<Cliente> clientes = Repository.getInstance().getAll(Cliente.class);
		PropertyListView<Cliente> table = new PropertyListView<Cliente>("table", clientes) {

			@Override
			protected void populateItem(ListItem<Cliente> item) {

				item.add(new Label("id"));
				item.add(new Label("nombre"));
				item.add(new Label("abono"));
				item.add(new Label("contrato"));
				item.add(new Label("edad"));
				item.add(new Label("sms"));
				item.add(new Label("voz"));
				item.add(new Label("puntos"));
				item.add(createDeleteAction(item.getModelObject()));
				item.add(createLoginLink(item.getModelObject()));
				item.add(createLogoutLink(item.getModelObject()));
			}
		};

		return table;
	}
}
