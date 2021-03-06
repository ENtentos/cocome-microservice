package org.cocome.reportsservice.service;

import java.util.Collection;
import java.util.Formatter;

import javax.ejb.Remote;
import javax.ejb.Stateless;

import org.cocome.productsclient.client.ProductClient;
import org.cocome.productsclient.client.ProductSupplierClient;
import org.cocome.productsclient.domain.Product;
import org.cocome.productsclient.domain.ProductSupplier;
import org.cocome.reportsservice.domain.Report;
import org.cocome.storesclient.client.StockItemClient;
import org.cocome.storesclient.client.StoreClient;
import org.cocome.storesclient.client.TradingEnterpriseClient;
import org.cocome.storesclient.domain.StockItem;
import org.cocome.storesclient.domain.Store;
import org.cocome.storesclient.domain.TradingEnterprise;

/**
 * This class reimplements the original reporting functionality from 
 * <a href="https://github.com/cocome-community-case-study/cocome-cloud-jee-platform-migration/blob/04c26157ea37c307ac4a2b9a623233e83e70e26b/cocome-maven-project/cloud-logic-service/cloud-enterprise-logic/enterprise-logic-ejb/src/main/java/org/cocome/tradingsystem/inventory/application/reporting/ReportingServer.java">
 * org.cocome.tradingsystem.inventory.application.reporting.ReportingServer
 * </a>.
 *
 */
@Remote
@Stateless
public class HTMLReportGenerator implements ReportGenerator {
	private TradingEnterpriseClient enterpriseClient = new TradingEnterpriseClient();
	private StoreClient storeClient = new StoreClient();
	private StockItemClient stockItemClient = new StockItemClient();
	private ProductSupplierClient supplierClient = new ProductSupplierClient();
	private ProductClient productClient = new ProductClient();
	
	
	@Override
	public Report getEnterpriseDeliveryReport(long enterpriseId) {
		final TradingEnterprise enterprise = this.enterpriseClient.find(enterpriseId);
		final Formatter report = new Formatter();
		appendReportHeader(report);
		appendDeliveryReport(enterprise, report);
		appendReportFooter(report);
		return createReportTO(report);
	}

	@Override
	public Report getStoreStockReport(long storeId) {
		final Store store = this.storeClient.find(storeId);
		final Formatter report = new Formatter();
		appendReportHeader(report);
		appendStoreReport(store, report);
		appendReportFooter(report);
		return createReportTO(report);
	}

	@Override
	public Report getEnterpriseStockReport(long enterpriseId) {
		TradingEnterprise enterprise = this.enterpriseClient.find(enterpriseId);
		Formatter report = new Formatter();
		appendReportHeader(report);
		appendEnterpriseReport(enterprise, report);
		appendReportFooter(report);
		return createReportTO(report);
	}
	
	// Private helper methods
	
	private void appendDeliveryReport(TradingEnterprise enterprise, Formatter output) {
		this.appendTableHeader(output, "Supplier ID", "Supplier Name", "Mean Time To Delivery");

		for (ProductSupplier supplier : this.supplierClient.findByEnterprise(enterprise.getId())) {
			// See org.cocome.tradingsystem.inventory.data.enterprise.EnterpriseQueryProvider.java
			// in cocome-cloud-jee-platform-migration.
			// It's not implemented there, so we use the same fixed value (0) here.
			//
			//
			//final long mtd = this.enterpriseQuery.getMeanTimeToDelivery(
			//		supplier, enterprise);
			final long mtd = 0;

			this.appendTableRow(output, supplier.getId(), supplier.getName(),
					(mtd != 0) ? mtd : "N/A"); // NOCS
		}

		this.appendTableFooter(output);
	}

	private void appendStoreReport(final Store store, final Formatter output) {
		output.format("<h3>Report for %s at %s, id %d</h3>\n", store.getName(),
				store.getLocation(), store.getId());

		this.appendTableHeader(output, "StockItem ID", "Product Name",
				"Amount", "Min Stock", "Max Stock");

		//

		final Collection<StockItem> stockItems = this.stockItemClient.findByStore(store.getId());

		for (final StockItem si : stockItems) {
			Product product = this.productClient.find(si.getProductId());
			this.appendTableRow(output, si.getId(), product.getName(),
					si.getAmount(), si.getMinStock(), si.getMaxStock());
		}

		this.appendTableFooter(output);
	}

	private void appendEnterpriseReport(final TradingEnterprise enterprise, final Formatter output) {
		output.format("<h2>Stock report for %s</h2>\n", enterprise.getName());

		for (final Store store : this.storeClient.findByEnterprise(enterprise.getId())) {
			this.appendStoreReport(store, output);
		}
	}

	private Report createReportTO(final Formatter report) {
		final Report result = new Report();
		result.setReportText(report.toString());
		return result;
	}

	private Formatter appendReportFooter(final Formatter output) {
		return output.format("</body></html>\n");
	}

	private Formatter appendReportHeader(final Formatter output) {
		return output.format("<html><body>\n");
	}

	private void appendTableHeader(final Formatter output,
			final String... names) {
		output.format("<table>\n<tr>");
		for (final String name : names) {
			output.format("<th>%s</th>", name);
		}
		output.format("</tr>\n");
	}

	private void appendTableRow(final Formatter output, final Object... values) {
		output.format("<tr>");
		for (final Object value : values) {
			output.format("<td>%s</td>", value);
		}
		output.format("</tr>\n");
	}

	private void appendTableFooter(final Formatter output) {
		output.format("</table><br/>\n");
	}
}
