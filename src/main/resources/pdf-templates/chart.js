/*<![CDATA[*/

let datatable = [[ ${ datatable } ]];
let columnsTable = [ [ ${ columnsTable } ]];
let backgroundColor = [[ ${ backgroundColor } ]];
let borderColor = [[ ${ borderColor } ]];
let type = [[ ${ type } ]];
console.log( '====================================' );
console.log( type );
console.log( '====================================' );
/*]]>*/


const ctx = document.getElementById( 'myChart' ).getContext( '2d' );
const myChart = new Chart( ctx, {
  type,
  data: {
    labels: columnsTable,
    datasets: [ {
      label: 'Nombre de votes',
      data: datatable,
      backgroundColor,
      borderColor,
      // borderWidth: 1
    } ]
  },
  options: {
    legend: {},
    responsive: true,
    //   scales: {
    //     y: {
    //       beginAtZero: true
    //     }
    //   }
  }
} );