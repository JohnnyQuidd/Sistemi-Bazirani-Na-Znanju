import React, { useMemo } from "react";
import '../style/UsersTable.css'
import { useTable } from "react-table";

function UsersTable({ usersData, updateUser }) {

  const categories = ['LOW', 'MODERATE', 'HIGH', 'EXTREME'];
  const riskPropertyHandler = (event, user) => {
      user['riskCategory'] = categories[event.target.options.selectedIndex];
      updateUser(user);
  }  
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
      Header: "Risk category",
      accessor: "riskCategory",
      Cell: (tableProps) => (
          <select className="risk-category"
          value={tableProps.row.original['riskCategory']}
          onChange={(event) => riskPropertyHandler(event, tableProps.row.original)}>
              <option>LOW</option>
              <option>MODERATE</option>
              <option>HIGH</option>
              <option>EXTREME</option>
          </select>
      ),
    },
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

export default UsersTable;
