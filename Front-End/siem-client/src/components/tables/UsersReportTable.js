import React, { useMemo } from "react";
import '../style/UsersTable.css'
import { useTable } from "react-table";

function UsersReportTable({ usersData}) {
  const COLUMNS = [
    {
      Header: "Id",
      accessor: "id",
    },
    {
      Header: "Username",
      accessor: "username",
    },
    {
      Header: "Last time active",
      accessor: "lastTimeUserWasActive",
    },
    {
      Header: "Risk Category",
      accessor: "riskCategory",
    }
  ];

  const cols = useMemo(() => COLUMNS, []);

  const table = useTable({
    columns: cols,
    data: usersData,
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
                  {column.render("Header")}
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

export default UsersReportTable;
