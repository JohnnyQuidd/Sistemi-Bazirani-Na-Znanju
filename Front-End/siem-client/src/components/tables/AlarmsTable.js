import React, { useMemo } from "react";
import "../style/LogsTable.css";
import { useTable } from "react-table";

function AlarmsTable({ alarmsData, expandModal }) {
  const COLUMNS = [
    {
      Header: "Id",
      accessor: "id",
    },
    {
      Header: "Message",
      accessor: "message",
    },
    {
        Header: "Status",
        accessor: "factStatus",
    },
    {
      Header: "Timestamp",
      accessor: "timestamp",
    },
    {
      Header: "Details",
      accessor: "details",
      Cell: (tableProps) => (
        <button
        className="logs-button"
        onClick={() => expandModal(tableProps.row.original)}>More...</button>
      ),
    },
  ];

  const cols = useMemo(() => COLUMNS, []);

  const table = useTable({
    columns: cols,
    data: alarmsData,
  });
  const { getTableProps, getTableBodyProps, headerGroups, rows, prepareRow } =
    table;

  return (
    <div className="table-div">
      <table {...getTableProps()}>
        <thead>
          {headerGroups.map((headerGroup) => (
            <tr {...headerGroup.getHeaderGroupProps()}>
              {headerGroup.headers.map((column) => (
                <th {...column.getHeaderProps()}>
                  {" "}
                  {column.render("Header")}{" "}
                </th>
              ))}
            </tr>
          ))}
        </thead>
        <tbody {...getTableBodyProps()}>
          {rows.map((row) => {
            prepareRow(row);
            return (
              <tr {...row.getRowProps()}>
                {row.cells.map((cell) => {
                  return (
                    <td {...cell.getCellProps()}> {cell.render("Cell")} </td>
                  );
                })}
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
}

export default AlarmsTable;
