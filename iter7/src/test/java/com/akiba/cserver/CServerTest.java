package com.akiba.cserver;

import java.io.File;

import junit.framework.TestCase;

import com.akiba.cserver.message.GetAutoIncrementValueRequest;
import com.akiba.cserver.message.GetAutoIncrementValueResponse;
import com.akiba.cserver.message.ScanRowsRequest;
import com.akiba.cserver.message.WriteRowRequest;
import com.akiba.cserver.message.WriteRowResponse;
import com.akiba.cserver.store.PersistitStore;
import com.akiba.message.AkibaConnection;
import com.akiba.message.Message;
import com.akiba.message.MessageRegistryBase;
import com.akiba.network.AkibaNetworkHandler;
import com.akiba.network.NetworkHandlerFactory;

public class CServerTest extends TestCase implements CServerConstants {

	private final static File DATA_PATH = new File("/tmp/data");

	private final static RowDef ROW_DEF = RowDef.createRowDef(1234,
			new FieldDef[] { new FieldDef("a", FieldType.INT),
					new FieldDef("b", FieldType.INT),
					new FieldDef("c", FieldType.INT) }, "test",
			"group_table_test", new int[] { 0 });

	@Override
	public void setUp() throws Exception {
		Util.cleanUpDirectory(DATA_PATH);
		PersistitStore.setDataPath(DATA_PATH.getPath());
		MessageRegistryBase.reset();
	}

	// Currently it seems we can't run two tests in one class
	// with our NetworkHandler, so this one is removed.
	//
	public void dontTestWriteRowResponse() throws Exception {
		final CServer cserver = new CServer();
		cserver.getRowDefCache().putRowDef(ROW_DEF);
		cserver.start();
		try {
			final AkibaNetworkHandler networkHandler = NetworkHandlerFactory
					.getHandler("localhost", "8080", null);
			final AkibaConnection connection = AkibaConnection
					.createConnection(networkHandler);

			final WriteRowRequest request = createWriteRowRequest();

			final WriteRowResponse response = (WriteRowResponse) connection
					.sendAndReceive(request);
			assertEquals(1, response.getResultCode());

			networkHandler.disconnectWorker();
			NetworkHandlerFactory.closeNetwork();

		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			cserver.stop();
		}
	}

	public void testGetAutoIncrementRequestResponse() throws Exception {
		final CServer cserver = new CServer();
		cserver.getRowDefCache().putRowDef(ROW_DEF);
		cserver.start();
		try {
			final AkibaNetworkHandler networkHandler = NetworkHandlerFactory
					.getHandler("localhost", "8080", null);
			final AkibaConnection connection = AkibaConnection
					.createConnection(networkHandler);
			Message request;
			Message response;

			request = new GetAutoIncrementValueRequest(ROW_DEF.getRowDefId());
			response = connection.sendAndReceive(request);
			assertEquals(-1, ((GetAutoIncrementValueResponse) response)
					.getValue());

			request = createWriteRowRequest();
			response = connection.sendAndReceive(request);
			assertEquals(OK, ((WriteRowResponse) response).getResultCode());

			request = new GetAutoIncrementValueRequest(ROW_DEF.getRowDefId());
			response = connection.sendAndReceive(request);
			assertEquals(1, ((GetAutoIncrementValueResponse) response)
					.getValue());

			networkHandler.disconnectWorker();
			NetworkHandlerFactory.closeNetwork();

		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			cserver.stop();
		}
	}

	private WriteRowRequest createWriteRowRequest() {
		final RowData rowData = new RowData(new byte[64]);
		rowData.createRow(ROW_DEF, new Object[] { 1, 2, 3 });
		final WriteRowRequest request = new WriteRowRequest();
		request.setRowData(rowData);
		return request;
	}

}
