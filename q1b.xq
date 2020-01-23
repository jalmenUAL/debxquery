declare function local:title($b)
{
$b/title
};

<bib>
 {
  for $b in db:open('bstore')/bib/book
  where $b/publisher = 'Addison-Wesley' and $b/rate > 3
  return
    <book rate='{ $b/rate}'>
     { local:title($b) }
    </book>
 }
</bib> 